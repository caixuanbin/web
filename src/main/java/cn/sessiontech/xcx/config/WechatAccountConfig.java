package cn.sessiontech.xcx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xbcai
 * @classname WechatAccountConfig
 * @description 微信账号
 * @date 2019/3/19 9:37
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {
    /**
     * 公众平台id
     */
    private String appID;
    /**
     * 公众平台密钥
     */
    private String appsecret;
}

