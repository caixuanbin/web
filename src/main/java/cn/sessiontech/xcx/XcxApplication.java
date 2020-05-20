package cn.sessiontech.xcx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author  xbcai
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@ServletComponentScan
@EnableCaching
@EnableScheduling
@EnableJpaAuditing
public class XcxApplication {

    public static void main(String[] args) {
        SpringApplication.run(XcxApplication.class, args);
    }

}
