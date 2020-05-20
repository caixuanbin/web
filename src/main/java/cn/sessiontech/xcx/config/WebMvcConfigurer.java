package cn.sessiontech.xcx.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author xbcai
 * @classname WebMvcConfigurer
 * @description 配置图片访问映射路径
 * @date 2019/7/1 21:33
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurationSupport {
    @Value("${file.absolutePath}")
    private String absolutePath;

    @Value("${file.staticAccessPath}")
    private String staticAccessPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //和页面有关的静态目录都放在项目的static目录下
        registry.addResourceHandler(staticAccessPath).addResourceLocations(absolutePath);
        //上传的图片在D盘下的OTA目录下，访问路径如：http://localhost:8081/OTA/d3cf0281-bb7f-40e0-ab77-406db95ccf2c.jpg
        //其中OTA表示访问的前缀。"file:D:/OTA/"是文件真实的存储路径
        // registry.addResourceHandler("/OTA/**").addResourceLocations("file:D:/OTA/");
    }

}
