package cn.sessiontech.xcx.config;

import cn.sessiontech.xcx.filter.GConfigFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author  xbcai
 * 全局过滤器配置
 */
@Configuration
public class FilterConfiguration {
    @Bean
    FilterRegistrationBean globalFilterBean(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new GConfigFilter());
        bean.addUrlPatterns("/*");
        return bean;
    }

}
