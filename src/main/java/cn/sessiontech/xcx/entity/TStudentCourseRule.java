package cn.sessiontech.xcx.entity;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Administrator
 * @classname TStudentCourseRule
 * @description 学生规则表
 * @date 2019/10/4 15:06
 */
@Data
@Entity(name = "t_student_course_rule")
public class TStudentCourseRule extends BaseEntity {
    @Column(length = 50)
    private String userId;
    /**
     * 礼拜一上课规则，多个时间段用英文逗号隔开
     * 例如：08:00,09:00,22:00
     */
    @Column(length = 100)
    private String ruleMonday;
    /**
     * 礼拜二上课规则
     */
    @Column(length = 100)
    private String ruleTuesday;
    /**
     * 礼拜三上课规则
     */
    @Column(length = 100)
    private String ruleWednesday;
    /**
     * 礼拜四上课规则
     */
    @Column(length = 100)
    private String ruleThursday;
    /**
     * 礼拜五上课规则
     */
    @Column(length = 100)
    private String ruleFriday;
    /**
     * 礼拜六上课规则
     */
    @Column(length = 100)
    private String ruleSaturday;
    /**
     * 礼拜日上课规则
     */
    @Column(length = 100)
    private String ruleSunday;
    /**
     * 规则生效时间 yyyy-MM-dd
     */
    @Column(length = 20)
    private String takeEffectTime;
}
