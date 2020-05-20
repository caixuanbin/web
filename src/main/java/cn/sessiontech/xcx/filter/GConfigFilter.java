package cn.sessiontech.xcx.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author xbai
 * 全局过滤器
 */
@Slf4j
@RestController
public class GConfigFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("---GConfigFilter---doFilter--");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("---GConfigFilter---init--");
    }

    @Override
    public void destroy() {
        log.info("---GConfigFilter---destroy--");
    }
}
