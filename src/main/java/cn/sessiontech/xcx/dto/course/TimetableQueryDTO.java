package cn.sessiontech.xcx.dto.course;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xbcai
 * @classname TimetableQueryDTO
 * @description 课程表查询条件
 * @date 2019/10/4 19:43
 */
@Data
public class TimetableQueryDTO {
    @NotNull(message = "当前页码不能为空")
    private Integer currentPage;
    @NotNull(message = "一页显示多少条记录不能为空")
    private Integer pageSize;
    private String beginTime;
    private String endTime;
    private String studentUserName;
    private String teacherUserName;
    private String studentUserId;
}
