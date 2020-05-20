package cn.sessiontech.xcx.dto.course;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xbai
 * @classname BuyCourseDTO
 * @description 购买课时
 * @date 2019/10/4 20:56
 */
@Data
public class BuyCourseDTO {
    @NotBlank(message = "学生id不能为空")
    private String studentUserId;
    @NotBlank(message = "老师id不能为空")
    private String teacherUserId;
    /**
     * 1：购买；2：赠送；3：扣减
     */
    @NotBlank(message = "购买类型不能为空")
    private String buyStatus;
    /**
     * 课节数
     */
    @NotBlank(message = "课节数不能为空")
    private String classNum;

    private String classRemarks;

}
