package cn.sessiontech.xcx.entity;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Administrator
 * @classname TStudentCourseTobeArranged
 * @description 待安排课程表
 * @date 2019/10/5 10:44
 */
@Data
@Entity(name = "t_student_course_tobe_arranged")
public class TStudentCourseTobeArranged extends BaseEntity {
    /**
     * t_student_school_timetable 已安排课程表id
     */
    @Column(length = 50)
    private String schoolTimetableId;
    @Column(length = 50)
    private String studentUserId;
    @Column(length = 50)
    private String studentUserName;
    @Column(length = 50)
    private String teacherUserId;
    @Column(length = 50)
    private String teacherUserName;
    /**
     * 上课时间
     */
    @Column(length = 50)
    private String classTime;
    /**
     * 课程状态
     * 1:待安排；2：已安排；3：已取消；
     */
    @Column(length = 5)
    private String courseStatus;
    /**
     * 是否有效 1：有效；2：无效；
     * 默认值 1
     */
    @Column(length = 5)
    private String isValid;
}
