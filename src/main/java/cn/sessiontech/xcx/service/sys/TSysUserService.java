package cn.sessiontech.xcx.service.sys;

import cn.sessiontech.xcx.entity.sys.TSysUser;
import cn.sessiontech.xcx.service.base.BaseService;
import cn.sessiontech.xcx.vo.sys.TSaasHomeSysUserVO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Administrator
 * @classname TSysUserService
 * @description 系统用户
 * @date 2019/9/10 14:57
 */
public interface TSysUserService extends BaseService<TSysUser> , UserDetailsService {
    /**
     * 根据用户ID获取用户登录的权限信息
     * @param UserId 用户ID
     * @return 返回页面权限结构信息
     */
    TSaasHomeSysUserVO getRoleMenusByUserId(String UserId);

    /**
     * 返回校验的信息
     * @param username 账号
     * @param openid 微信openid
     * @throws UsernameNotFoundException
     */
    UserDetails loadUserByUsername(String username, String openid) throws UsernameNotFoundException;
}
