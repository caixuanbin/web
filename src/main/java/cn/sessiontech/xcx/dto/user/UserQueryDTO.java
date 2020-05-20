package cn.sessiontech.xcx.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xbcai
 * @classname UserQueryDTO
 * @description 查询用户信息
 * @date 2019/10/4 14:09
 */
@Data
public class UserQueryDTO {
    @NotBlank(message = "当前页码不能为空")
    private Integer currentPage;
    @NotBlank(message = "一页显示多少条记录不能为空")
    private Integer pageSize;
    @NotBlank(message = "用户身份不能为空")
    private String userIdentity;
    private String keyword;
}
