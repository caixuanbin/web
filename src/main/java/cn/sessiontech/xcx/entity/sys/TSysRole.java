package cn.sessiontech.xcx.entity.sys;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author xbcai
 * @classname TSysUserRole
 * @description 角色表
 * @date 2019/9/10 14:34
 */
@Entity(name = "t_sys_role")
@Data
public class TSysRole extends BaseEntity {
    @Column(length = 50)
    private String roleName;
    @Column(length = 100)
    private String roleDesc;
    /**
     * 权限标识
     * 大写，以ROLE_开头为标识保存，供spring security鉴权使用，例如：ROLE_ADMIN
     */
    @Column(length = 20)
    private String roleAuth;
    /**
     * 标识
     * Y:可用；N:不可用；
     */
    @Column(length = 2)
    private String flag;
}
