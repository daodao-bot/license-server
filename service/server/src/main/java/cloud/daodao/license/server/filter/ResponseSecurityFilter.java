package cloud.daodao.license.server.filter;

import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.error.AppError;
import cloud.daodao.license.common.error.AppException;
import cloud.daodao.license.common.helper.TraceHelper;
import cloud.daodao.license.common.util.security.AesUtil;
import cloud.daodao.license.server.config.AppConfig;
import cloud.daodao.license.server.constant.FilterConstant;
import cloud.daodao.license.server.helper.FilterHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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
    private AppConfig appConfig;

    @Resource
    private TraceHelper traceHelper;

    @Resource
    private FilterHelper filterHelper;

    @SuppressWarnings("unchecked")
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 禁用 Content-Length，启用分块传输编码
        response.setHeader("Transfer-Encoding", "chunked");
        response.setContentLength(-1); // 禁用 Content-Length

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

        ResponseWrapper responseWrapper = new ResponseWrapper(response);

        filterChain.doFilter(request, responseWrapper);

        byte[] bytes = responseWrapper.getContent();
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
            // response.getOutputStream().write(bytes);
            // return;
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

        int length = bodyBytes.length;
        String trade = traceHelper.traceId();
        String time = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setContentLength(length);
        response.setHeader(AppConstant.X_SECURITY, security);
        response.setHeader(AppConstant.X_TRACE, trade);
        response.setHeader(AppConstant.X_TIME, time);
        response.getOutputStream().write(bodyBytes);

    }

    private class ResponseWrapper extends HttpServletResponseWrapper {

        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        private final PrintWriter printWriter = new PrintWriter(outputStream);

        public ResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new ServletOutputStream() {
                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {

                }

                @Override
                public void write(int b) throws IOException {
                    outputStream.write(b);
                }

                @Override
                public void write(byte[] b) throws IOException {
                    outputStream.write(b);
                }

                @Override
                public void write(byte[] b, int off, int len) throws IOException {
                    outputStream.write(b, off, len);
                }

                @Override
                public void flush() throws IOException {
                    outputStream.flush();
                }

            };
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return printWriter;
        }

        @Override
        public void flushBuffer() throws IOException {
            flush();
            super.flushBuffer();
        }

        public void flush() {
            try {
                printWriter.flush();
                printWriter.close();
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        public byte[] getContent() {
            flush();
            return outputStream.toByteArray();
        }

    }

}
