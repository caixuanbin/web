package cn.sessiontech.xcx.entity;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Administrator
 * @classname TUserWechatRelation
 * @description 登录者与openid绑定关系
 * @date 2019/10/4 11:55
 */
@Data
@Entity(name = "t_user_wechat_relation")
public class TUserWechatRelation extends BaseEntity {
    @Column(length = 50)
    private String openid;
    /**
     * 登录账号
     */
    @Column(length = 50)
    private String username;
}
