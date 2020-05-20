package cn.sessiontech.xcx.enums;

/**
 * @author xbcai
 * @classname BusinessEnum
 * @description 业务枚举
 * @date 2019/10/4 21:14
 */
public enum BusinessEnum {
    /**
     * 购买课时类型
     */
    COURSE_BUY_STATUS_BUY("1","购买"),
    COURSE_BUY_STATUS_GIVE("2","赠送"),
    COURSE_BUY_STATUS_CUT("3","扣减"),
    /**
     * 购买课时后是否已经安排
     */
    COURSE_BUY_IS_ARRANGED_NO("1","未安排"),
    COURSE_BUY_IS_ARRANGED_RUNNING("2","安排中"),
    COURSE_BUY_IS_ARRANGED_YES("3","已安排"),
    /**
     * 上课完后评价
     */
    CLASS_ORDER_EVALUATE_NO("1","未评价"),
    CLASS_ORDER_EVALUATE_YES("2","已评价"),
    /**
     * 请假后的课程表待安排
     */
    COURSE_TOBE_ARRANGED_DAP("1","待安排"),
    COURSE_TOBE_ARRANGED_YAP("2","已安排"),
    COURSE_TOBE_ARRANGED_YQX("3","已取消"),
    /**
     * 课程状态
     */
    COURSE_STATUS_YSK("1","已上课"),
    COURSE_STATUS_YYY("2","已预约"),
    COURSE_STATUS_ASK_LEAVE_BKKS("3","请假-不扣课时"),
    COURSE_STATUS_ASK_LEAVE_KKS("4","请假-扣课时"),
    COURSE_STATUS_KK("5","旷课"),
    COURSE_STATUS_ASK_LEAVE_TEACHER("6","老师请假"),

    /**
     * 课程工单
     */
    COURSE_ORDER_HAD_PRODUCE("1","未生成课程工单"),
    COURSE_ORDER_HAD_NOT_PRODUCE("2","已生成课程工单"),
    /**
     * 表单状态
     */
    FORM_ID_EFFECTIVE("1","有效"),
    FORM_ID_INVALID("2","失效"),

    ASK_LEAVEL_YES("1","请假操作"),
    ASK_LEAVEL_NO("2","取消请假操作"),
    /**
     * 数据
     */
    DATA_EFFECTIVE("1","有效"),
    DATA_INVALID("2","失效"),
    /**
     * 1:未读；2：已读；
     */
    MESSAGE_NOT_READ("1","未读"),
    MESSAGE_HAD_READ("2","已读"),
    /**
     * 1:未发送；2：已发送
     */
    MESSAGE_NOT_SEND("1","未发送"),
    MESSAGE_HAD_SEND("2","已发送"),
    /**
     * 上课提醒消息
     */
    GO_TO_CLASS_HAD_NOT_SEND_MESSAGE("1","未发送上课提醒"),
    GO_TO_CLASS_HAD_SEND_MESSAGE_6_HOUR("2","6小时内发送上课提醒"),
    GO_TO_CLASS_HAD_SEND_MESSAGE_HALF_HOUR("3","30分钟内发送上课提醒"),

    ;
    private String code;
    private String value;

    BusinessEnum(String code, String value) {
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
