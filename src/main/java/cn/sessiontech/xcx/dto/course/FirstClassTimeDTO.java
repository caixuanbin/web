package cn.sessiontech.xcx.dto.course;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Administrator
 * @classname FirstClassTimeDTO
 * @description 课时预约
 * @date 2019/10/4 22:30
 */
@Data
public class FirstClassTimeDTO {
    @NotBlank(message = "首次上课时间不能为空")
    private String firstOnClassTime;
    @NotBlank(message = "id不能为空")
    private String id;
    private Integer currentPage;
    private Integer pageSize;
}
