package cn.sessiontech.xcx.entity.sys;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author xbcai
 * @classname TSysAuth
 * @description 系统权限表
 * @date 2019/9/12 16:55
 */
@Data
@Entity(name = "t_sys_auth")
public class TSysAuth extends BaseEntity {
    /**
     * 角色权限标识
     */
    @Column(length = 20)
    private String roleAuth;
    /**
     * 权限名称
     */
    @Column(length = 20)
    private String authName;
    /**
     * 权限对应访问权限路径
     */
    @Column(length = 100)
    private String authPath;
}
