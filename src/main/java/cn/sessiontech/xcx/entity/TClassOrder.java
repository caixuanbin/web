package cn.sessiontech.xcx.entity;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author xbcai
 * @classname TClassOrder
 * @description 课程工单
 * @date 2019/10/4 22:50
 */
@Data
@Entity(name = "t_class_order")
public class TClassOrder extends BaseEntity {
    /**
     * 上课时间
     */
    @Column(length = 50)
    private String classTime;
    /**
     * 学生id
     */
    @Column(length = 50)
    private String studentUserId;
    /**
     * 学生姓名
     */
    @Column(length = 50)
    private String studentUserName;
    /**
     * 老师id
     */
    @Column(length = 50)
    private String teacherUserId;
    /**
     * 老师姓名
     */
    @Column(length = 50)
    private String teacherUserName;
    /**
     *1：已上课；2：请假-不扣课时；3：请假-扣课时；4：旷课；
     */
    private String classStatus;
    /**
     *1：未评价；2：已评价；
     */
    @Column(length = 5)
    private String isEvaluate;
    /**
     * 评价信息
     */
    @Column(length = 250)
    private String evaluateInfo;
    /**
     * 评价用户id
     */
    @Column(length = 50)
    private String evaluateUserId;
    /**
     * 课程表id
     */
    @Column(length = 50)
    private String timeTableId;
}
