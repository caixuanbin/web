package cn.sessiontech.xcx.entity.sys;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author xbai
 * @classname TSysUserRole
 * @description 用户角色关联表
 * @date 2019/9/11 17:07
 */
@Data
@Entity(name = "t_sys_user_role")
public class TSysUserRole extends BaseEntity {
    @Column(length = 32)
    private String userId;
    @Column(length = 32)
    private String roleId;
}
