package cn.sessiontech.xcx.dto.weixin;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Administrator
 * @classname WeixinFormDTO
 * @description 微信表单id集合
* @date 2019/10/21 22:09
 */
@Data
public class WeixinFormDTO {
    @NotBlank(message = "ids集合不能为空")
    String ids;
}
