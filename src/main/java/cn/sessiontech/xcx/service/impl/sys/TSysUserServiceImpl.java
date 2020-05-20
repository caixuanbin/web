package cn.sessiontech.xcx.service.impl.sys;

import cn.sessiontech.xcx.entity.sys.TSysMenu;
import cn.sessiontech.xcx.entity.sys.TSysUser;
import cn.sessiontech.xcx.enums.RedisEnum;
import cn.sessiontech.xcx.repository.sys.TSysMenuRepository;
import cn.sessiontech.xcx.repository.sys.TSysRoleRepository;
import cn.sessiontech.xcx.repository.sys.TSysUserRepository;
import cn.sessiontech.xcx.service.base.BaseServiceImpl;
import cn.sessiontech.xcx.service.sys.TSysUserService;
import cn.sessiontech.xcx.utils.ConverterUtils;
import cn.sessiontech.xcx.utils.RedisKeyUtils;
import cn.sessiontech.xcx.vo.sys.TSaasHomeSysMenuVO;
import cn.sessiontech.xcx.vo.sys.TSaasHomeSysRoleVO;
import cn.sessiontech.xcx.vo.sys.TSaasHomeSysUserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @classname TSysUserServiceImpl
 * @description 用户模块
 * @date 2019/9/10 15:02
 */
@Service
public class TSysUserServiceImpl extends BaseServiceImpl<TSysUser> implements TSysUserService {
    @Autowired
    private TSysUserRepository tSysUserRepository;
    @Autowired
    private TSysRoleRepository tSysRoleRepository;
    @Autowired
    private TSysMenuRepository tSysMenuRepository;
    @Autowired
    private RedisKeyUtils redisKeyUtils;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      return null;
    }
    @Override
    public UserDetails loadUserByUsername(String username,String openid) throws UsernameNotFoundException {
        TSysUser sysUser = tSysUserRepository.findByUsername(username);
        if(sysUser==null){
            throw new UsernameNotFoundException(username);
        }
        Map<String,String>  map = new HashMap<>();
        map.put("id",sysUser.getId());
        map.put("userIdentity",sysUser.getUserIdentity()==null?"":sysUser.getUserIdentity());
        map.put("fullName",sysUser.getFullName());
        map.put("username",sysUser.getUsername());
        map.put("tel",sysUser.getTel()==null?"":sysUser.getTel());
        map.put("userSex",sysUser.getUserSex());
        //将系统用户信息设置到缓存
        redisKeyUtils.hmset(RedisEnum.HOME_SYS_USER.getCode()+openid,map);
        String[] userRoleAuths = tSysRoleRepository.getUserRoleAuths(sysUser.getId());
        List<String> collect = Arrays.stream(userRoleAuths).map(item ->item.split(",")[0]).collect(Collectors.toList());
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.createAuthorityList(collect.toArray(new String[collect.size()]));
        return new User(sysUser.getUsername(),"", grantedAuthorities);
    }
    @Override
    public TSaasHomeSysUserVO getRoleMenusByUserId(String userId) {
        List<TSysMenu> allMenu = tSysMenuRepository.findAll(Sort.by("sortBy"));
        Optional<TSysUser> sysUser = tSysUserRepository.findById(userId);
        TSaasHomeSysUserVO vo = ConverterUtils.convert(sysUser.orElse(new TSysUser()),TSaasHomeSysUserVO.class);
        List<TSaasHomeSysRoleVO> roles = new ArrayList<>();
        String[] userRoleAuths = tSysRoleRepository.getUserRoleAuths(userId);
         Arrays.stream(userRoleAuths).forEach(item ->{
                    String[] array = item.split(",");
                    //开始设置角色及对应的菜单
                    TSaasHomeSysRoleVO role = new TSaasHomeSysRoleVO();
                    role.setId(array[1]);
                    role.setRoleAuth(array[0]);
                    role.setRoleName(array[2]);
                    String[] menuIds = tSysMenuRepository.getMenuId(array[1]);
                    List<TSysMenu> roleMenus = allMenu.stream().filter(it->{
                        boolean flag = false;
                        for (String menuId : menuIds) {
                            if (StringUtils.equals(it.getId(), menuId)) {
                                flag = true;
                                break;
                            }
                        }
                        return flag;
                    }).collect(Collectors.toList());
                    List<TSaasHomeSysMenuVO> sysMenu = ConverterUtils.convert(roleMenus, TSaasHomeSysMenuVO.class);
                    List<TSaasHomeSysMenuVO> oneLevel = sysMenu.stream().filter(one->one.getMenuLevel()==1).collect(Collectors.toList());
                    Map<String, List<TSaasHomeSysMenuVO>> twoLevel = sysMenu.stream().filter(one -> one.getMenuLevel() == 2).collect(Collectors.groupingBy(TSaasHomeSysMenuVO::getPId));
                    oneLevel.forEach(ol->ol.setChildMenus(twoLevel.get(ol.getId())));
                    role.setMenus(oneLevel);
                    roles.add(role);
                    //完成对应角色设置的对应菜单列表
                }
        );
        vo.setRoles(roles);
        return vo;
    }
}
