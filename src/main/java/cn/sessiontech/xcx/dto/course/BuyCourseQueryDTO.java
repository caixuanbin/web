package cn.sessiontech.xcx.dto.course;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xbcai
 * @classname BuyCourseQueryDTO
 * @description 课时购买查询
 * @date 2019/10/4 22:07
 */
@Data
public class BuyCourseQueryDTO {
    @NotNull(message = "当前页码不能为空")
    private Integer currentPage;
    @NotNull(message = "一页显示多少条记录不能为空")
    private Integer pageSize;
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 学生姓名
     */
    private String studentUserName;
    /**
     * 老师姓名
     */
    private String teacherUserName;
    /**
     * 1：未安排；2：已安排
     */
    private String isArranged;
}
