package cn.sessiontech.xcx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xbcai
 * @classname ProjectUrlConfig
 * @description 微信属性
 * @date 2019/3/19 9:40
 */
@Data
@Component
@ConfigurationProperties(prefix = "pj")
public class ProjectUrlConfig {
    /**
     * 微信公众平台授权url
     */
    public String wechatMpAuthorize;

}