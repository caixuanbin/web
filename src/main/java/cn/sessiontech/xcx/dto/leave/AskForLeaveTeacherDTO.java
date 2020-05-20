package cn.sessiontech.xcx.dto.leave;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xbcai
 * @classname askForLeaveTeacherDTO
 * @description 老师请假
 * @date 2019/10/5 18:56
 */
@Data
public class AskForLeaveTeacherDTO {
    @NotBlank(message = "老师id不能为空")
    private String teacherUserId;
    @NotBlank(message = "请假开始时间不能为空")
    private String leaveBeginTime;
    @NotBlank(message = "请假结束时间不能为空")
    private String leaveEndTime;
}
