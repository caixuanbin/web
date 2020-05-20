package cn.sessiontech.xcx.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Administrator
 * @classname LoginUserDTO
 * @description 登录用户信息
 * @date 2019/8/12 11:38
 */
@Data
public class LoginUserDTO {
    @NotBlank(message = "账号不能为空")
    private String username;
    private String password;
    @NotBlank(message = "openid不能为空")
    private String openid;
}
