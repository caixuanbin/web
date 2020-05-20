package cn.sessiontech.xcx.entity;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author xbcai
 * @classname TSysMessage
 * @description 消息
 * @date 2019/10/4 23:54
 */
@Data
@Entity(name = "t_sys_message")
public class TSysMessage extends BaseEntity {
    @Column(length = 50)
    private String userId;
    /**
     * 用户姓名
     */
    @Column(length = 50)
    private String userName;
    @Column(length = 50)
    private String openid;
    @Column(length = 250)
    private String sysContent;
    /**
     * 1:未读；2：已读；
     */
    @Column(length = 5)
    private String isRead;
    /**
     * 1:未发送；2：已发送
     */
    @Column(length = 5)
    private String isSend;
    @Column(length = 20)
    private String sendTime;
}
