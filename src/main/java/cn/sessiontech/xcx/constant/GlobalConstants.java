package cn.sessiontech.xcx.constant;

/**
 * @author xbcai
 * @classname Constants
 * @description 全局常量属性
 * @date 2019/6/11 16:09
 */
public final class GlobalConstants {
    /**
     * 下发验证码到用户手机上面的验证码有效期提醒文字，要和SMS_VALIDITY一致
     */
    public static final String SMS_VALIDITY_TEXT="5分钟";
    /**
     * 短信验证码有效期
     */
    public static final int SMS_VALIDITY=5*60;

    /**
     * 验证码错误达到5次便锁定
     */
    public static final Long ERROR_COUNT = 5L;
    /**
     * 锁定5分钟
     */
    public static final int LOCK_TIME = 5*60;

    /**
     * 开始时间后缀
     */
    public static final String START_TIME_SUFFIX=" 00:00:00";
    /**
     * 开始时间后缀
     */
    public static final String START_TIME_SUFFIX_H_M=" 00:00";
    /**
     * 结束时间后缀
     */
    public static final String END_TIME_SUFFIX=" 23:59:59";
    /**
     * 结束时间后缀
     */
    public static final String END_TIME_SUFFIX_H_M=" 23:59";
    /**
     * 倒序
     */
    public static final String DESC="desc";
    /**
     * 升序
     */
    public static final String ASC="asc";

    /**
     * 链接超时时间 单位为毫秒
     */
    public final static int CONNECT_TIME_OUT = 2000;

    public final static String YYYY_MM_DD_HH_MM_SS="yyyy-MM-dd HH:mm:ss";



    /**
     * TOKEN有效时间 10080分钟(7天) 这个设置在token里面返回给前端 单位是毫秒
     */
    public static final int EXPIRE_IN=1008*10*60*1000;
    /**
     * TOKEN有效刷新时间 20160分钟（14天） 这个设置在redis里面，单位是秒
     */
    public static final int REFRESH_EXPIRE_IN = 10080*2*60;
    /**
     * 签名key
     */
    public static final String SIGNING_KEY = "spring-security-@Jwt!&Secret^#";
    /**
     * 查询全部标识
     */
    public final static String FIND_ALL="0";
    /**
     * 学生请假
     */
    public final static String ASK_LEAVE_STUDENT="askLeaveStudent";
    /**
     * 取消学生请假
     */
    public final static String CANCEL_LEAVE_STUDENT="cancelLeaveStudent";
    /**
     * 设置学生旷课
     */
    public final static String SET_STUDENT_KK="setStudentKk";
    /**
     * 老师请假
     */
    public final static String ASK_LEAVE_TEACHER="askLeaveTeacher";
    /**
     * 设置第一次上课时间即预约上课时间
     */
    public final static String SET_FIRST_CLASS_TIME="setFirstClassTime";
    /**
     * 扣减课程
     */
    public final static String DEDUCTION_CLASS="deductionClass";
    /**
     * 更新上课规则
     */
    public final static String UPDATE_COURSE_RULE="updateCourseRule";

    public final static String RULE_MONDAY="monday";
    public final static String RULE_TUESDAY="tuesday";
    public final static String RULE_WEDNESDAY="wednesday";
    public final static String RULE_THURSDAY="thursday";
    public final static String RULE_FRIDAY="friday";
    public final static String RULE_SATURDAY="saturday";
    public final static String RULE_SUNDAY="sunday";
    /**
     * 一节课时长 默认30分钟
     */
    public final static long classTimeLenght=30;




}
