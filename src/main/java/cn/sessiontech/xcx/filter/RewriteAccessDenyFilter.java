package cn.sessiontech.xcx.filter;

import cn.sessiontech.xcx.common.Result;
import cn.sessiontech.xcx.utils.JsonUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author xbcai
 * @classname RewriteAccessDenyFilter
 * @description 暂时没用到
 * @date 2019/6/18 14:36
 */
public class RewriteAccessDenyFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException {
        try {
            chain.doFilter(request, response);
            System.out.println("自定义过滤器------------");
        } catch (Exception e) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().println(JsonUtils.obj2String(Result.success(e)));
        }
    }
}
