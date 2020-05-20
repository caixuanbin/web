package cn.sessiontech.xcx.enums;

/**
 * @author xbcai
 * @classname ResultCodeEnum
 * @description 全局消息返回码 需要严格遵照http 状态吗规范使用
 * @date 2019/5/21 8:44
 */
public enum ResultCodeEnum {
    /**
     * 成功
     */
    SUCCESS(200, "OK"),
    /**
     *请求失败
     */
    BAD_REQUEST(400, "请求失败"),
    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未经授权的"),
    /**
     * 被禁止调用
     */
    FORBIDDEN(403, "被禁止的"),
    /**
     * 访问不到
     */
    NOT_FOUND(404, "找不到该资源"),
    /**
     * 不允许的方法
     */
    METHOD_NOT_ALLOWED(405, "不允许的方法"),
    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(408, "请求超时"),
    /**
     * 冲突
     */
    CONFLICT(409, "冲突"),
    /**
     * 不存在
     */
    GONE(410, "不存在"),
    /**
     * 有效载荷太大
     */
    PAYLOAD_TOO_LARGE(413, "有效载荷太大"),
    /**
     * 请求实体太大
     */
    REQUEST_ENTITY_TOO_LARGE(413, "请求实体太大"),
    /**
     * 异常失败
     */
    EXPECTATION_FAILED(417, "异常失败"),
    /**
     * 请求过于频繁
     */
    TOO_MANY_REQUESTS(429, "请求过于频繁"),
    /**
     * 内部服务器错误
     */
    INTERNAL_SERVER_ERROR(500, "内部服务器错误"),
    /**
     * 坏网关
     */
    BAD_GATEWAY(502, "坏网关"),
    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    /**
     * 网关超时
     */
    GATEWAY_TIMEOUT(504, "网关超时"),
    /**
     * 需要网络身份验证
     */
    NETWORK_AUTHENTICATION_REQUIRED(511, "需要网络身份验证"),
    /**
     * 1000~9000 自定义业务码
     */

    LOGIN_LOCKING(1004,"账号被锁住,请5分钟后再试"),

    LOCK_NECESSARY_PARAMS(1005,"必填参数为空"),

    /**
     * 10000~99999 api接口业务码
     */
    /**
     * 用户名或密码错误
     */
    TOKEN_USER_LOGIN_ERROR(10002,"用户名或密码不正确"),
    /**
     * 签名信息被非法修改
     */
    TOKEN_ILLEGAL_SIGN(10003,"非法签名"),
    /**
     * 数据已存在
     */
    DATA_EXISTS(10004,"数据已存在"),
    /**
     * 非法数据
     */
    DATA_ILLEGAL(10005,"数据非法"),
    /**
     * 不支持该jwt
     */
    UNSUPPORTED_JWT(10006,"不支持该jwt"),
    /**
     * 畸形的jwt
     */
    MALFORMED_JWT(10007,"畸形的jwt"),
    /**
     * 非法证据
     */
    ILLEGAL_ARGUMENT(10008,"非法证据"),

    ASK_LEAVE_ILLEGAL(10009,"请假无效"),
    /**
     * 非法设置旷课
     */
    SET_TRUANCY_ILLEGAL(10010,"设置旷课无效"),
    /**
     * 取消请假无效
     */
    CANCEL_LEAVE_ILLEGAL(10011,"取消请假无效"),
    /**
     * 旧有token失效，重新获取新的token
     */
    TOKEN_REFRESH_TIME(10000,"重新获取了token"),
    /**
     * 刷新token不在有效时间内，需要重新登录授权
     */
    TOKEN_REFRESH_OUT_TIME(10001,"获取token失效，需重新登录"),
    /**
     * 账号绑定
     */
    ILLEGAL_LOGIN(10002,"该账号已绑定，请使用绑定的微信号登录微信重新登录"),
    /**
     * 账号已存在
     */
    ACCOUNT_HAD_REGISTER(10003,"该账号已存在！"),



    ;
    public int code;
    public String message;

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
