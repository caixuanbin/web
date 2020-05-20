package cn.sessiontech.xcx.enums;

/**
 * @author xbcai
 * @classname LoginEnum
 * @description 登录相关信息
 * @date 2019/6/25 10:13
 */
public enum LoginEnum {
    /**
     * 登录用户被锁
     */
    LOGIN_USER_LOCK("loginUserLock:","登录用户被锁"),
    LOGIN_USER_IDENTIFY_STUDENT("student","学生"),
    LOGIN_USER_IDENTIFY_TEACHER("teacher","老师"),
    LOGIN_USER_IDENTIFY_PARENT("parent","家长"),
    LOGIN_USER_IDENTIFY_OPERATOR("operator","运营者"),
    LOGIN_USER_IDENTIFY_SUPER("super","超级用户"),
    LOGIN_USER_SEX_MAN("man","男"),
    LOGIN_USER_SEX_WOMAN("woman","女"),
    login_user_flag_Y("y","可用"),
    login_user_flag_n("n","不可用")
    ;
    private String key;
    private String name;

    LoginEnum(String key, String name) {
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
