package cn.sessiontech.xcx.dto.leave;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xbcai
 * @classname AskForLeaveDTO
 * @description 请假
 * @date 2019/10/5 16:16
 */
@Data
public class AskForLeaveDTO {
    @NotBlank(message = "请假id不能为空")
    private String ids;
}
