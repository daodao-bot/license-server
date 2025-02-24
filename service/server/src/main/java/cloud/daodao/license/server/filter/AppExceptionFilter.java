package cloud.daodao.license.server.filter;

import cloud.daodao.license.common.error.AppException;
import cloud.daodao.license.server.helper.FilterHelper;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Order(-2)
@Component
public class AppExceptionFilter implements Filter {

    @Resource
    private FilterHelper filterHelper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (AppException e) {
            log.error(e.getMessage(), e);
            filterHelper.responseException((HttpServletRequest) request, (HttpServletResponse) response);
        }

    }

}
