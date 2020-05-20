package cn.sessiontech.xcx.intercepter;

import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author Administrator
 * @classname GlodalIntercepter
 * @description TODO
 * @date 2019/8/24 22:04
 */
public class GlodalIntercepter extends FilterSecurityInterceptor {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        super.doFilter(request, response, chain);
    }
}
