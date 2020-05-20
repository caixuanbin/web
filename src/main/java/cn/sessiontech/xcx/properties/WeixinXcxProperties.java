package cn.sessiontech.xcx.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xbcai
 * @classname WeixinXcxProperties
 * @description 小程序公共参数封装
 * @date 2019/9/18 21:31
 */
@Data
@Component
@ConfigurationProperties(prefix="xcx")
public class WeixinXcxProperties {
    private String appid;
    private String secret;
    private String grantType;
    private String askLeave;
}
