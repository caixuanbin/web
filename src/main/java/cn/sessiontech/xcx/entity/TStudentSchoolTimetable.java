package cn.sessiontech.xcx.entity;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author xbcai
 * @classname TStudentSchoolTimetable
 * @description 课程表
 * @date 2019/10/4 19:30
 */
@Data
@Entity(name = "t_student_school_timetable")
public class TStudentSchoolTimetable extends BaseEntity {
    @Column(length = 50)
    private String studentUserId;
    @Column(length = 50)
    private String studentUserName;
    @Column(length = 50)
    private String teacherUserName;
    @Column(length = 50)
    private String teacherUserId;
    @Column(length = 50)
    private String classTime;
    /**
     * 1:已上课；2：已预约；3：请假-不扣课时；4：请假-扣课时；5：旷课; 6：老师请假；
     */
    @Column(length = 10)
    private String classStatus;
    /**
     * -- 都是针对已上完课的
     * 1:未生成课程工单；2：已生成课程工单；
     */
    @Column(length = 5)
    private String isProduceOrder;
    /**
     * 1:有效；2：无效；
     * 默认为1
     */
    @Column(length = 5)
    private String isValid;
    /**
     * 1:未发送上课提醒；2：已发送上课提醒(6小时内)；3：再次发送上课提醒（30分钟内）
     */
    @Column(length = 5)
    private String isSendClass;
}
