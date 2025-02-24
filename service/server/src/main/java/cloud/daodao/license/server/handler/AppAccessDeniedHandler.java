package cloud.daodao.license.server.handler;

import cloud.daodao.license.common.constant.AppConstant;
import cloud.daodao.license.server.constant.FilterConstant;
import cloud.daodao.license.server.helper.FilterHelper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author DaoDao
 */
@Component
public class AppAccessDeniedHandler implements AccessDeniedHandler {

    @Resource
    private FilterHelper filterHelper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        Object originUri = request.getAttribute(FilterConstant.X_ORIGIN_URI);
        if (null != originUri && ((String) originUri).startsWith("/" + AppConstant.API + "/")) {
            filterHelper.responseException(request, response);
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());
    }

}
