package cn.sessiontech.xcx.entity;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author xbcai
 * @classname TTeacherLeaveTime
 * @description 老师请假表
 * @date 2019/10/5 19:05
 */
@Data
@Entity(name = "t_teacher_leave_time")
public class TTeacherLeaveTime extends BaseEntity {
    @Column(length = 50)
    private String teacherUserId;
    @Column(length = 50)
    private String teacherUserName;
    @Column(length = 50)
    private String leaveBeginTime;
    @Column(length = 50)
    private String leaveEndTime;
    /**
     * 1：生效中；2：已生效；3：已取消；
     */
    @Column(length = 5)
    private String leaveStatus;
    @Column(length = 250)
    private String leaveRemarks;

}
