package cloud.daodao.license.server.helper;

import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.error.AppError;
import cloud.daodao.license.common.error.AppException;
import cloud.daodao.license.common.model.Response;
import cloud.daodao.license.server.config.AppConfig;
import cloud.daodao.license.server.constant.FilterConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class FilterHelper {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private AppConfig appConfig;

    public Boolean doSecurity(HttpServletRequest request) {
        Boolean doSecurity = Boolean.TRUE;

        Boolean apiSecurity = appConfig.getApiSecurity();
        if (Boolean.FALSE.equals(apiSecurity)) {
            doSecurity = Boolean.FALSE;
        }

        String uri = request.getRequestURI();
        if (!uri.startsWith("/" + AppConstant.API + "/")) {
            doSecurity = Boolean.FALSE;
        }

        if (Arrays.asList(FilterConstant.NO_SECURITY_URI).contains(uri)) {
            doSecurity = Boolean.FALSE;
        }

        if (!HttpMethod.POST.matches(request.getMethod())) {
            doSecurity = Boolean.FALSE;
        }

        return doSecurity;
    }

    public void responseException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Response<?> data;
        Object exception = request.getAttribute(FilterConstant.X_EXCEPTION);
        if (null != exception) {
            AppException appException = (AppException) exception;
            data = new Response<>(appException);
        } else {
            data = new Response<>(new AppException(AppError.TOKEN_ERROR));
        }
        response.getWriter().write(objectMapper.writeValueAsString(data));
    }

}
