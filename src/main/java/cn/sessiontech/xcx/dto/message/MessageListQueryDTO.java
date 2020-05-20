package cn.sessiontech.xcx.dto.message;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @author xbcai
 * @classname MessageListQueryDTO
 * @description 消息管理查询
 * @date 2019/10/5 0:02
 */
@Data
public class MessageListQueryDTO {
    @NotNull(message = "当前页码不能为空")
    private Integer currentPage;
    @NotNull(message = "一页显示多少条记录不能为空")
    private Integer pageSize;
    private String keyword;
    private String beginTime;
    private String endTime;
}
