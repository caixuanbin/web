package cn.sessiontech.xcx.enums;

/**
 * @author xbai
 * @classname TokenEnum
 * @description 鉴权相关
 * @date 2019/6/17 21:00
 */
public enum TokenEnum {
    /**
     * api请求刷新权限
     */
    TOKEN_REFRESH("tokenRefresh:","客户端刷新权限")
    ;
    private String code;
    private String value;

    TokenEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
