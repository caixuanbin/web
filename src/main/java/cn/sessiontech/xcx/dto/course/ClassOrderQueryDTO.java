package cn.sessiontech.xcx.dto.course;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xbcai
 * @classname ClassOrderQueryDTO
 * @description 课程工单
 * @date 2019/10/4 23:00
 */
@Data
public class ClassOrderQueryDTO {
    @NotNull(message = "当前页码不能为空")
    private Integer currentPage;
    @NotNull(message = "一页显示多少条记录不能为空")
    private Integer pageSize;
    private String keyword;
    private String beginTime;
    private String endTime;
    private String studentUserName;
    private String teacherUserName;

}
