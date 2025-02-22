package cloud.daodao.license.server.handler;

import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.common.error.AppError;
import cloud.daodao.license.common.error.AppException;
import cloud.daodao.license.common.model.Response;
import cloud.daodao.license.server.constant.FilterConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author DaoDao
 */
@Component
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Object originUri = request.getAttribute(FilterConstant.X_ORIGIN_URI);
        if (null != originUri && ((String) originUri).startsWith("/" + AppConstant.API + "/")) {
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
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

}
