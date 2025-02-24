package cloud.daodao.license.server.handler;

import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.server.constant.FilterConstant;
import cloud.daodao.license.server.helper.FilterHelper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
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
    private FilterHelper filterHelper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Object originUri = request.getAttribute(FilterConstant.X_ORIGIN_URI);
        if (null != originUri && ((String) originUri).startsWith("/" + AppConstant.API + "/")) {
            filterHelper.responseException(request, response);
            return;
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

}
