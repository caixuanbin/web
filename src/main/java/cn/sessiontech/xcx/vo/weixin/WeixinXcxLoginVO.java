package cn.sessiontech.xcx.vo.weixin;

import lombok.Data;

/**
 * @author Administrator
 * @classname WeixinXcxLoginVO
 * @description 小程序登录返回实体类
 * @date 2019/9/18 21:23
 */
@Data
public class WeixinXcxLoginVO {
    /**
     * 用户唯一标识
     */
    private String openid;
    /**
     * 会话密钥
     */
    private String session_key;
    /**
     * 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回
     */
    private String unionid;
    /**
     * 错误码
     */
    private String errcode;
    /**
     * 错误信息
     */
    private String errmsg;


}
