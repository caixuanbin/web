package cn.sessiontech.xcx.dto.home;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xbcai
 * @classname TeacherQueryDTO
 * @description 首页老师查询资料
 * @date 2019/10/5 11:34
 */
@Data
public class TeacherQueryDTO {
    @NotNull(message = "当前页码不能为空")
    private Integer currentPage;
    @NotNull(message = "一页显示多少条记录不能为空")
    private Integer pageSize;
}
