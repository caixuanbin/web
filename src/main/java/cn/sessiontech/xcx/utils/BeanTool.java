package cn.sessiontech.xcx.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author xbcai
 * @classname BeanTool
 * @description Bean 工具类
 * @date 2019/4/22 17:51
 */
@Component
public class BeanTool implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @SuppressWarnings("unchecked")

    public static <T> T getBean(String beanId) {
        return (T) applicationContext.getBean(beanId);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return (T) applicationContext.getBean(requiredType);
    }

    /**
     * Spring容器启动后，会把 applicationContext 给自动注入进来，
     * 然后我们把 applicationContext 赋值到静态变量中，方便后续拿到容器对象
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanTool.applicationContext = applicationContext;
    }
}
