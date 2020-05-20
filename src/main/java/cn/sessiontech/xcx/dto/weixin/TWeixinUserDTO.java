package cn.sessiontech.xcx.dto.weixin;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Administrator
 * @classname TWeixinUserDTO
 * @description 微信登录信息
 * @date 2019/10/21 21:18
 */
@Data
public class TWeixinUserDTO {
    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickName;
    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    @NotBlank(message = "性别不能为空")
    private String gender;
    /**
     * 用户个人资料填写的省份
     */
    @NotBlank(message = "身份不能为空")
    private String province;
    /**
     * 普通用户个人资料填写的城市
     */
    @NotBlank(message = "城市不能为空")
    private String city;
    /**
     * 国家，如中国为CN
     */
    @NotBlank(message = "国家不能为空")
    private String country;
    /**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），
     * 用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
     */
    @NotBlank(message = "头像不能为空")
    private String avatarUrl;

}
