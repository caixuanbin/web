package cn.sessiontech.xcx.provider;

import cn.sessiontech.xcx.entity.TUserWechatRelation;
import cn.sessiontech.xcx.service.TUserWechatRelationService;
import cn.sessiontech.xcx.service.sys.TSysUserService;
import cn.sessiontech.xcx.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author xbcai
 * @classname CustomAuthenticationProvider
 * @description 权限检验
 * @date 2019/6/17 11:33
 */
@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private TSysUserService tSaasHomeSysUserService;
    @Autowired
    private TUserWechatRelationService tUserWechatRelationService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证的用户名 & openid
        String username = authentication.getName();
        String openid = authentication.getCredentials().toString();
        // 认证逻辑
        UserDetails userDetails = tSaasHomeSysUserService.loadUserByUsername(username,openid);
        if (null != userDetails) {
            //记录登录者与账号的关系
            ThreadUtils.getOhterThreadPool().execute(()->{
                TUserWechatRelation wechat = new TUserWechatRelation();
                wechat.setUsername(username);
                wechat = tUserWechatRelationService.findEntityByParams(wechat);
                if(wechat==null){
                    wechat = new TUserWechatRelation();
                    wechat.setOpenid(openid);
                    wechat.setUsername(username);
                }else{
                    wechat.setOpenid(openid);
                    wechat.setUpdateTime(new Date());
                }
                tUserWechatRelationService.saveAndFlush(wechat);
            });
          return new UsernamePasswordAuthenticationToken(username, openid, userDetails.getAuthorities());
        } else {
            throw new UsernameNotFoundException("用户不存在");
        }
    }

    /**
     * 是否可以提供输入类型的认证服务
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
