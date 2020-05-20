package cn.sessiontech.xcx.constant;

/**
 * @author xbcai
 * @classname WeixinXcxConstants
 * @description 小程序URL
 * @date 2019/9/18 21:28
 */
public class WeixinXcxConstants {
    /**
     *微信token有效时间
     */
    public static final int EXPIRE_TIME=6000;
    /**
     * 登录凭证校验
     */
    public static final String JSCODE2SESSION="https://api.weixin.qq.com/sns/jscode2session";
    /**
     * 发送订阅消息
     */
    public static final String SUBSCRIBE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send";
    /**
     * 获取token
     */
    public static  String ACESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    /**
     * 订阅消息
     */
    public static final String SUB_MESSAGE_URL ="https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=#token";
}
