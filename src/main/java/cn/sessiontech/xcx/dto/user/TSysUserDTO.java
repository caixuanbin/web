package cn.sessiontech.xcx.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xbcai
 * @classname TSysUserDTO
 * @description 新增用户信息
 * @date 2019/10/4 13:48
 */
@Data
public class TSysUserDTO {
    /**
     * 账号-这里是微信号
     */
    private String username;

    /**
     * 性别
     */
    @NotBlank(message = "性别不能为空")
    private String userSex;
    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String fullName;

    /**
     * 身份 super:超级用户,operator：运营者,student：学生,teacher：老师,parent：家长,
     */
    @NotBlank(message = "身份不能为空")
    private String userIdentity;
    /**
     * 电话
     */
    private String tel;
    /**
     * 用户类型（老师角色才有）
     */
    private String userType;
    /**
     * 有效请假提前时间(老师角色才有) 单位是分钟
     */
    private Integer userEffectiveLeaveTime;
    /**
     * 家长（只有学生才有）
     */
    private String userParentId;

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
    private String ruleFriday;
    /**
     * 礼拜六上课规则
     */
    private String ruleSaturday;
    /**
     * 礼拜日上课规则
     */
    private String ruleSunday;

}
