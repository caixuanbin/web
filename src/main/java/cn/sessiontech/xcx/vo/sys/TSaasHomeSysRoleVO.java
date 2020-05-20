package cn.sessiontech.xcx.vo.sys;

import lombok.Data;

import java.util.List;

/**
 * @author xbcai
 * @classname TSaasHomeSysRoleVO
 * @description 角色
 * @date 2019/9/11 17:48
 */
@Data
public class TSaasHomeSysRoleVO {
    private String id;
    private String roleName;
    private String roleAuth;
    private List<TSaasHomeSysMenuVO> menus;
}
