package cn.sessiontech.xcx.enums;

/**
 *缓存key
 * @author xbcai
 */
public enum RedisEnum {
	/**
	 * 会话用户信息
	 */
	LOGIN_SESSION_USER("loginSessionUser","会话用户"),
	/**
	 * 获取登录验证码
	 */
	LOGIN_SMS_VERIFY_CODE("loginSmsVerifyCode:","登录验证码"),
	/**
	 * 忘记密码
	 */
	FORGET_PASSWD_SMS_VERIFY_CODE("forgetPasswdSmsVerifyCode:","忘记密码验证码"),


	/**
	 * key: sysUser:+登录账号
	 * value: map集合
	 */
	HOME_SYS_USER("sysUser:","系统用户信息")



	;
	
	private int key;
	private String code;
	private String value;
	
	private RedisEnum() {}

	private RedisEnum(int key) {
		this.key=key;
	}
	
	private RedisEnum(int key, String value) {
		this.key=key;
		this.value=value;
	}
	
	private RedisEnum(int key, String code, String value) {
		this.key=key;
		this.code=code;
		this.value=value;
	}
	
	private RedisEnum(String code) {
		this.code=code;
	}
	
	private RedisEnum(String code, String value) {
		this.code=code;
		this.value=value;
	}

	public int getKey() {
		return key;
	}

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}
}
