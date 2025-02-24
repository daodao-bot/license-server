package cloud.daodao.license.server.filter;

import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.error.AppError;
import cloud.daodao.license.common.error.AppException;
import cloud.daodao.license.common.helper.TraceHelper;
import cloud.daodao.license.common.util.security.AesUtil;
import cloud.daodao.license.server.constant.FilterConstant;
import cloud.daodao.license.server.helper.FilterHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

/**
 * @author DaoDao
 */
@Slf4j
@Order(1)
@Component
@WebFilter(urlPatterns = {"/" + AppConstant.API + "/**"})
public class ResponseSecurityFilter implements Filter {

    @Resource
    private TraceHelper traceHelper;

    @Resource
    private FilterHelper filterHelper;

    @SuppressWarnings("unchecked")
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Boolean doSecurity = filterHelper.doSecurity(request);

        if (!doSecurity) {
            filterChain.doFilter(request, response);
            return;
        }

        Object o = request.getAttribute(AppConstant.X_SECURITY);
        if (null == o) {
            filterChain.doFilter(request, response);
            return;
        }

        String aesKey = (String) request.getAttribute(FilterConstant.X_AES_KEY);
        String aesIv = (String) request.getAttribute(FilterConstant.X_AES_IV);

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(request, responseWrapper);

        byte[] bytes = responseWrapper.getContentAsByteArray();
        if (bytes.length == 0) {
            response.getOutputStream().write(bytes);
            return;
        }

        String contentType = responseWrapper.getContentType();
        if (null == contentType || !contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            response.getOutputStream().write(bytes);
            return;
        }

        String security = (String) o;

        ObjectMapper objectMapper = new ObjectMapper();
        LinkedHashMap<String, Object> bodyMap = objectMapper.readValue(bytes, LinkedHashMap.class);
        Object data = bodyMap.get("data");
        if (null == data) {
            data = new LinkedHashMap<>();
        }

        String dataPlains = objectMapper.writeValueAsString(data);
        String dataCipher;

        try {
            if (security.equals(AppConstant.AES)) {
                dataCipher = AesUtil.encrypt(aesKey, aesIv, dataPlains, AesUtil.Encode.BASE64);
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

        bodyMap.put("data", dataCipher);

        byte[] bodyBytes = objectMapper.writeValueAsBytes(bodyMap);

        log.info("X > : {}", new String(bodyBytes));

        String trace = traceHelper.traceId();
        String time = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.setHeader(AppConstant.X_SECURITY, security);
        response.setHeader(AppConstant.X_TRACE, trace);
        response.setHeader(AppConstant.X_TIME, time);

        response.getOutputStream().write(bodyBytes);

    }

}
