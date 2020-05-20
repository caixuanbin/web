package cn.sessiontech.xcx.handler;

import cn.sessiontech.xcx.common.Result;
import cn.sessiontech.xcx.enums.ResultCodeEnum;
import cn.sessiontech.xcx.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xbcai
 * @classname TokenAccessDeniedHandler
 * @description token handler
 * @date 2019/6/18 14:09
 */
@Slf4j
@Component
public class TokenAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        Result result = Result.fail(ResultCodeEnum.FORBIDDEN,request.getRequestURI()+" "+accessDeniedException.getMessage());
        response.getWriter().println(JsonUtils.obj2String(result));
    }

}
