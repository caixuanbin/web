package cn.sessiontech.xcx.dto.leave;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xbcai
 * @classname TeacherLeaveListQueryDTO
 * @description 老师请假查询条件
 * @date 2019/10/5 19:12
 */
@Data
public class TeacherLeaveListQueryDTO {
    @NotNull(message = "当前页码不能为空")
    private Integer currentPage;
    @NotNull(message = "一页显示多少条记录不能为空")
    private Integer pageSize;
    private String beginTime;
    private String endTime;
}
