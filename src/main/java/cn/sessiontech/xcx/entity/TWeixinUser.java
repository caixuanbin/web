package cn.sessiontech.xcx.entity;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author xbcai
 * @classname TSaasWeixinUser
 * @description 公众号的用户信心
 * @date 2019/6/25 14:57
 */
@Entity(name = "t_weixin_user")
@Data
public class TWeixinUser extends BaseEntity {
    /**
     * 微信openid
     */
    @Column(length = 32)
    private String openid;
    /**
     * 昵称
     */
    @Column(length = 100)
    private String nickName;
    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    @Column(length = 2)
    private String gender;
    /**
     * 用户个人资料填写的省份
     */
    @Column(length = 20)
    private String province;
    /**
     * 普通用户个人资料填写的城市
     */
    @Column(length = 50)
    private String city;
    /**
     * 国家，如中国为CN
     */
    @Column(length = 50)
    private String country;
    /**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），
     * 用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
     */
    @Column(length = 200)
    private String avatarUrl;
    /**
     * 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
     */
    @Column(length = 200)
    private String privilege;
    /**
     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
     */
    @Column(length = 32)
    private String unionid;
    /**
     * 系统用户id
     */
    @Column(length = 50)
    private String userId;
    /**
     * 系统用户账号
     */
    @Column(length = 50)
    private String username;
    /**
     * 系统用户姓名
     */
    @Column(length = 50)
    private String fullName;
    /**
     * 身份 super:超级用户,operator：运营者,student：学生,teacher：老师,parent：家长,
     */
    @Column(length = 50)
    private String userIdentity;


}
