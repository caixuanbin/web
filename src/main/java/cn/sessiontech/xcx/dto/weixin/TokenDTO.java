package cn.sessiontech.xcx.dto.weixin;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xbai
 * @classname TokenDTO
 * @description 登录获取访问权限封装实体
 * @date 2019/6/17 14:51
 */
@Data
public class TokenDTO implements Serializable {

    private static final long serialVersionUID = 375616253953378312L;

    private String token;
    private Long expireIn;
    private String username;

    public TokenDTO(String token, Long expireIn) {
        this.token = token;
        this.expireIn = expireIn;
    }

    public TokenDTO(String token, Long expireIn, String username) {
        this.token = token;
        this.expireIn = expireIn;
        this.username = username;
    }

    public TokenDTO() {
    }
}
