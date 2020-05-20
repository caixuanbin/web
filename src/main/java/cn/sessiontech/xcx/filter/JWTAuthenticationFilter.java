package cn.sessiontech.xcx.filter;

import cn.sessiontech.xcx.common.Result;
import cn.sessiontech.xcx.constant.GlobalConstants;
import cn.sessiontech.xcx.enums.ResultCodeEnum;
import cn.sessiontech.xcx.enums.TokenEnum;
import cn.sessiontech.xcx.utils.BeanTool;
import cn.sessiontech.xcx.utils.JsonUtils;
import cn.sessiontech.xcx.utils.JwtTokenUtils;
import cn.sessiontech.xcx.utils.RedisKeyUtils;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 *   @author xbcai
 *    token的校验
 *  * 该类继承自BasicAuthenticationFilter，在doFilterInternal方法中，
 *  * 从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。
 *  * 如果校验通过，就认为这是一个取得授权的合法请求
 */
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    private RedisKeyUtils redisKeyUtils = BeanTool.getBean(RedisKeyUtils.class);

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        String header =req.getHeader("Authorization");
        if (StringUtils.isEmpty(header)||StringUtils.equals(header,"undefined")) {
            chain.doFilter(req, resp);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req,resp);
        if(authentication!=null){
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(req, resp);
        }

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request,HttpServletResponse resp) throws IOException {
        String token = request.getHeader("Authorization");
        if (token != null) {
            Claims body = null;
            try {
                body = Jwts.parser()
                        .setSigningKey(GlobalConstants.SIGNING_KEY)
                        .parseClaimsJws(token.replace("Bearer ", ""))
                        .getBody();
                log.info("body:{}",body);
            } catch (ExpiredJwtException e) {
                e.printStackTrace();
                printJson(resp,token,ExpiredJwtException.class,e.getMessage());
            } catch (UnsupportedJwtException e) {
                e.printStackTrace();
                printJson(resp,token,UnsupportedJwtException.class,e.getMessage());
            } catch (MalformedJwtException e) {
                e.printStackTrace();
                printJson(resp,token,MalformedJwtException.class,e.getMessage());
            } catch (SignatureException e) {
                e.printStackTrace();
                printJson(resp,token,SignatureException.class,e.getMessage());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                printJson(resp,token,IllegalArgumentException.class,e.getMessage());
            }
            if (body != null) {
                return new UsernamePasswordAuthenticationToken(body.getSubject(), null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }

    /**
     * 返回前端
     * @param resp 输出流
     * @param token token
     * @param exception 异常类型
     * @param exceptionMsg 异常消息信息
     * @throws IOException
     */
    public void printJson(HttpServletResponse resp,String token,Class<?> exception,String exceptionMsg) throws IOException{
        log.info("Class:{},message:{}",exception,exceptionMsg);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");
        Result result = Result.fail(ResultCodeEnum.EXPECTATION_FAILED,exceptionMsg);
        if(exception.equals(ExpiredJwtException.class)){
            Map<String,Object> claims = JwtTokenUtils.analysisToken(token.replace("Bearer ", ""));
            //在redis里面获取该用户的是否在刷新token有效期内
            String refreshToken = redisKeyUtils.get(TokenEnum.TOKEN_REFRESH.getCode()+claims.get("username"));

            if(StringUtils.isNotEmpty(refreshToken)){
                //在有效期内，重新生成token返回前端
                result = Result.fail(ResultCodeEnum.TOKEN_REFRESH_TIME,JwtTokenUtils.createTokenDTO(claims,claims.get("username").toString()));
            }else{
                //不在有效期内，不生成token,告诉前端需重新登录
                result = Result.fail(ResultCodeEnum.TOKEN_REFRESH_OUT_TIME);
            }
        }else if(exception.equals(UnsupportedJwtException.class)){
        }else if(exception.equals(MalformedJwtException.class)){
        }else if(exception.equals(SignatureException.class)){
            result = Result.fail(ResultCodeEnum.TOKEN_ILLEGAL_SIGN,exceptionMsg);
        }else if(exception.equals(IllegalArgumentException.class)){
        }
        resp.getWriter().println(JsonUtils.obj2String(result));
    }
}
