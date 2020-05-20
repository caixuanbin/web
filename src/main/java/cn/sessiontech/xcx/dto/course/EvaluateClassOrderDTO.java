package cn.sessiontech.xcx.dto.course;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xbcai
 * @classname EvaluateClassOrderDTO
 * @description 评价课程
 * @date 2019/10/4 23:41
 */
@Data
public class EvaluateClassOrderDTO {
    @NotBlank(message = "id不能为空")
    private String id;
    @NotBlank(message = "评价信息不能为空")
    private String evaluateInfo;
}
