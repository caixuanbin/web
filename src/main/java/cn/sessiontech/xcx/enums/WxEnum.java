package cn.sessiontech.xcx.enums;

/**
 * @author xbcai
 * @classname WxEnum
 * @description 微信相关
 * @date 2019/6/25 15:45
 */
public enum WxEnum {
    /**
     * 微信用户头像
     */
    WX_USER_HEADIMGURL("wxUserHeadimgurl:","微信用户头像链接"),
    WX_ACCESS_TOKEN("wxAccessToken","全局微信token")
    ;
    private String key;
    private String name;

    WxEnum(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
