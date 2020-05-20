package cn.sessiontech.xcx.constant;


import cn.sessiontech.xcx.utils.YmlUtils;

/**
 * @author xbcai
 * @classname UrlConstants
 * @description 所有的URL
 * @date 2019/6/26 22:04
 */
public final class ApiUrlConstants {
    /**
     * api url 前缀
     */
    public final static String URL_PREFIX = YmlUtils.getValue("api.urlPrefix");
    /**
     * 短信验证码
     */
    public final static String SMS_YZM_URL = URL_PREFIX+"/biz/sms/send/ktqx/verifyCode";

}
