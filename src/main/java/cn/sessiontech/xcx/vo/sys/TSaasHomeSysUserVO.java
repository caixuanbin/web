package cn.sessiontech.xcx.vo.sys;

import lombok.Data;

import java.util.List;

/**
 * @author xbcai
 * @classname TSaasHomeSysUserVO
 * @description 登录成功返回实体
 * @date 2019/9/11 17:45
 */
@Data
public class TSaasHomeSysUserVO {
    private String id;
    private String username;
    private String fullName;
    private String userIdentity;
    private String tel;
    private List<TSaasHomeSysRoleVO> roles;
}
