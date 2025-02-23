package cloud.daodao.license.server.filter;

import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.error.AppError;
import cloud.daodao.license.common.error.AppException;
import cloud.daodao.license.common.model.Serializer;
import cloud.daodao.license.common.util.security.AesUtil;
import cloud.daodao.license.server.config.AppConfig;
import cloud.daodao.license.server.constant.CacheConstant;
import cloud.daodao.license.server.constant.FilterConstant;
import cloud.daodao.license.server.helper.LicenseHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.stream.IntStream;

/**
 * @author DaoDao
 */
@Slf4j
@Order(-1)
@Component
@WebFilter(urlPatterns = {"/" + AppConstant.API + "/**"})
public class RequestSecurityFilter implements Filter {

    @Resource
    private AppConfig appConfig;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private LicenseHelper licenseHelper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();
        request.setAttribute(FilterConstant.X_ORIGIN_URI, uri);

        if (!uri.startsWith("/" + AppConstant.API + "/")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!HttpMethod.POST.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        Boolean apiSecurityEnabled = appConfig.getApiSecurityEnabled();

        String appId = request.getHeader(AppConstant.X_APP_ID);
        if (null != appId) {
            request.setAttribute(AppConstant.X_APP_ID, appId);
        }

        String security = request.getHeader(AppConstant.X_SECURITY);
        if (null != security) {
            request.setAttribute(AppConstant.X_SECURITY, security);
        }

        String trace = request.getHeader(AppConstant.X_TRACE);
        if (null != trace && !trace.isEmpty()) {
            String key = CacheConstant.TRACE + trace;
            String value = stringRedisTemplate.opsForValue().get(key);
            if (null == value) {
                stringRedisTemplate.opsForValue().set(key, trace, Duration.ofMinutes(2L));
            } else {
                AppException exception = new AppException(AppError.REQUEST_TRACE_DUPLICATE, trace);
                request.setAttribute(FilterConstant.X_EXCEPTION, exception);
                throw exception;
            }
        }

        String time = request.getHeader(AppConstant.X_TIME);
        if (null != time && !time.isEmpty()) {
            ZonedDateTime requestZonedDateTime;
            try {
                requestZonedDateTime = ZonedDateTime.parse(time, DateTimeFormatter.RFC_1123_DATE_TIME);
            } catch (DateTimeParseException e) {
                log.error(e.getMessage(), e);
                AppException exception = new AppException(AppError.REQUEST_TIME_ERROR, time);
                request.setAttribute(FilterConstant.X_EXCEPTION, exception);
                throw exception;
            }
            ZonedDateTime currentZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC);
            Duration duration = Duration.between(requestZonedDateTime, currentZonedDateTime);
            if (duration.toSeconds() > 180L) {
                AppException exception = new AppException(AppError.REQUEST_TIME_ERROR, time);
                request.setAttribute(FilterConstant.X_EXCEPTION, exception);
                throw exception;
            }
        }

        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        if (null == contentType || !contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (null == security) {
            if (apiSecurityEnabled) {
                AppException exception = new AppException(AppError.REQUEST_SECURITY_ERROR, "null");
                request.setAttribute(FilterConstant.X_EXCEPTION, exception);
                throw exception;
            } else {
                filterChain.doFilter(request, response);
                return;
            }
        } else {
            if (AppConstant.AES.equals(security)) {
                String license = licenseHelper.license(appId);
                // String aesKey = license.substring(0, 16);
                // String aesIv = license.substring(16, 32);
                String aesKey = IntStream.range(0, license.length()).filter(i -> i % 2 == 0).mapToObj(license::charAt).collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
                String aesIv = IntStream.range(0, license.length()).filter(i -> i % 2 == 1).mapToObj(license::charAt).collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
                request.setAttribute(FilterConstant.X_AES_KEY, aesKey);
                request.setAttribute(FilterConstant.X_AES_IV, aesIv);
            } else {
                AppException exception = new AppException(AppError.REQUEST_SECURITY_ERROR, security);
                request.setAttribute(FilterConstant.X_EXCEPTION, exception);
                throw exception;
            }
        }

        RequestWrapper requestWrapper = new RequestWrapper(request);

        filterChain.doFilter(requestWrapper, response);

    }

    private class RequestWrapper extends HttpServletRequestWrapper {

        private final HttpServletRequest request;

        public RequestWrapper(HttpServletRequest request) {
            super(request);
            this.request = request;
        }

        @SuppressWarnings("unchecked")
        @Override
        public ServletInputStream getInputStream() throws IOException {

            ServletInputStream inputStream = super.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            inputStream.transferTo(outputStream);
            byte[] bytes = outputStream.toByteArray();
            if (bytes.length == 0) {
                return super.getInputStream();
            }

            log.info("X < : {}", new String(bytes));

            ObjectMapper objectMapper = new ObjectMapper();
            LinkedHashMap<String, Object> bodyMap = objectMapper.readValue(bytes, LinkedHashMap.class);
            Object param = bodyMap.get("param");
            if (null == param) {
                return super.getInputStream();
            }
            String paramCipher;
            try {
                paramCipher = (String) param;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                AppException exception = new AppException(AppError.REQUEST_PARAM_ERROR, Serializer.toJson(param));
                request.setAttribute(FilterConstant.X_EXCEPTION, exception);
                throw exception;
            }
            if (paramCipher.isEmpty()) {
                return super.getInputStream();
            }

            String aesKey = null;
            Object ak = request.getAttribute(FilterConstant.X_AES_KEY);
            if (null != ak) {
                aesKey = (String) ak;
            }
            String aesIv = null;
            Object ai = request.getAttribute(FilterConstant.X_AES_IV);
            if (null != ai) {
                aesIv = (String) ai;
            }

            Object o = request.getAttribute(AppConstant.X_SECURITY);
            if (null == o) {
                AppException exception = new AppException(AppError.REQUEST_SECURITY_ERROR, "null");
                request.setAttribute(FilterConstant.X_EXCEPTION, exception);
                throw exception;
            }
            String security = (String) o;
            String paramPlains;
            try {
                if (security.equals(AppConstant.AES)) {
                    paramPlains = AesUtil.decrypt(aesKey, aesIv, paramCipher, AesUtil.Encode.BASE64);
                } else {
                    AppException exception = new AppException(AppError.REQUEST_SECURITY_ERROR, security);
                    request.setAttribute(FilterConstant.X_EXCEPTION, exception);
                    throw exception;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                AppException exception = new AppException(AppError.REQUEST_SECURITY_ERROR, e.getMessage());
                request.setAttribute(FilterConstant.X_EXCEPTION, exception);
                throw exception;
            }

            JsonNode paramNode = objectMapper.readTree(paramPlains);

            if (paramNode.isArray()) {
                ArrayList<Object> paramData = objectMapper.readValue(paramPlains, ArrayList.class);
                bodyMap.put("param", paramData);
            } else if (paramNode.isObject()) {
                LinkedHashMap<String, Object> paramData = objectMapper.readValue(paramPlains, LinkedHashMap.class);
                bodyMap.put("param", paramData);
            } else {
                AppException exception = new AppException(AppError.REQUEST_PARAM_ERROR, paramPlains);
                request.setAttribute(FilterConstant.X_EXCEPTION, exception);
                throw exception;
            }

            byte[] bodyBytes = objectMapper.writeValueAsBytes(bodyMap);

            return new RequestInputStream(bodyBytes);
        }

    }

    private static class RequestInputStream extends ServletInputStream {

        private final InputStream is;

        public RequestInputStream(byte[] body) {
            this.is = new ByteArrayInputStream(body);
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }

        @Override
        public int read() throws IOException {
            return this.is.read();
        }

    }

}
