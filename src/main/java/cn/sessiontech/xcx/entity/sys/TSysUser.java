package cn.sessiontech.xcx.entity.sys;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * @author xbcai
 * @classname TSysUser
 * @description 用户表
 * @date 2019/9/10 14:26
 */
@Entity(name = "t_sys_user")
@Data
public class TSysUser extends BaseEntity {
    /**
     * 账号
     */
    @Column(length = 20)
    private String username;
    /**
     * 密码
     */
    @Column(length = 100)
    private String password;
    /**
     * 性别
     */
    @Column(length = 20)
    private String userSex;
    /**
     * 姓名
     */
    @Column(length = 50)
    private String fullName;
    /**
     * 标识： Y:可用；N:不可用；
     */
    @Column(length = 2)
    private String flag;
    /**
     * 身份
     */
    @Column(length = 50)
    private String userIdentity;
    /**
     * 电话
     */
    @Column(length = 20)
    private String tel;
    /**
     * 用户类型（老师角色才有）
     */
    @Column(length = 50)
    private String userType;
    /**
     * 有效请假提前时间(老师角色才有) 单位是分钟
     */
    private Integer userEffectiveLeaveTime;
    /**
     * 家长（只有学生才有）
     */
    @Column(length = 50)
    private String userParentId;
    /**
     * 家长姓名（只有学生才有）
     */
    @Column(length = 50)
    private String userParentName;
    /**
     * 备注
     */
    @Column(length = 100)
    private String remarks;
    /**
     * 最后登录时间
     */
    private Date lastLoginTime;
}
