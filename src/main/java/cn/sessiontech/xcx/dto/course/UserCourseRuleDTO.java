package cn.sessiontech.xcx.dto.course;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

/**
 * @author xbcai
 * @classname UserCourseRuleDTO
 * @description 课程规则
 * @date 2019/10/26 11:10
 */
@Data
public class UserCourseRuleDTO {
    @NotBlank(message = "用户ID不能为空")
    private String userId;
    /**
     * 礼拜一上课规则，多个时间段用英文逗号隔开
     * 例如：08:00,09:00,22:00
     */
    private String ruleMonday;
    /**
     * 礼拜二上课规则
     */
    private String ruleTuesday;
    /**
     * 礼拜三上课规则
     */
    private String ruleWednesday;
    /**
     * 礼拜四上课规则
     */
    private String ruleThursday;
    /**
     * 礼拜五上课规则
     */
    @Column(length = 100)
    private String ruleFriday;
    /**
     * 礼拜六上课规则
     */
    private String ruleSaturday;
    /**
     * 礼拜日上课规则
     */
    private String ruleSunday;
    /**
     * 规则生效时间 yyyy-MM-dd
     */
    @NotBlank(message = "生效时间不能为空")
    private String takeEffectTime;
    /**
     * 修改类型：student 学生,teacher 老师
     */
    @NotBlank(message = "修改类型不能为空")
    private String modifyType;
}
