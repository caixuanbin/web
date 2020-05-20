package cn.sessiontech.xcx.aspect;

import cn.sessiontech.xcx.entity.LogEntity;
import cn.sessiontech.xcx.service.LogService;
import cn.sessiontech.xcx.utils.JsonUtils;
import cn.sessiontech.xcx.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author xbcai
 * @classname DemoCtrl
 * @description 注解日志 在需要打日志的地方加上相应的注解便可以将日志记录下来
 * @date 2019/5/20 20:53
 */
@Aspect
@Slf4j
@Component
public class AnnotationLogAspect {
    @Autowired
    private LogService logService;
    @Pointcut("@annotation(cn.sessiontech.xcx.aspect.AnnotationLogs)")
    public void controllerAspect(){
        System.out.println("--AnnotationLogAspect--controllerAspect--");
    }

    /**环绕日志打印
     */
    @Around("controllerAspect()")
    public Object handlerMethod(ProceedingJoinPoint pjp){
        Date createTime = new Date();
        Object result = null;
        try {
            result = pjp.proceed();
            saveLog(pjp,createTime,result);
        } catch (Throwable throwable) {
            saveLog(pjp,createTime,throwable.getMessage());
            throw new RuntimeException("调用发生异常："+throwable.getMessage());
        }
        return result;
    }

    /**
     *
     * @param pjp 切面
     * @param createTime 调用时间
     * @param result 返回值
     */
    public void saveLog(ProceedingJoinPoint pjp,Date createTime,Object result){
        ThreadUtils.getLogThreadPool().execute(()->{
            long elapsedTime = System.currentTimeMillis()-createTime.getTime();
            LogEntity logEntity = new LogEntity();
            MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
            Method method = methodSignature.getMethod();
            String description = method.getAnnotation(AnnotationLogs.class).description();
            logEntity.setCreateTime(createTime);
            logEntity.setClassName(method.getDeclaringClass().getName());
            logEntity.setMethodName(method.getName());
            logEntity.setMethodDesc(description);
            logEntity.setParams(org.apache.commons.lang3.StringUtils.join(pjp.getArgs(),","));
            logEntity.setReturnValue(JsonUtils.obj2String(result));
            logEntity.setUpdateTime(new Date());
            logEntity.setElapsedTime(elapsedTime);
            logService.saveAndFlush(logEntity);
            log.info("调用信息，耗时：{}，类名：{},方法名：{},方法描述：{},请求参数：{}",elapsedTime,method.getDeclaringClass().getName(),method.getName(),description, org.apache.commons.lang3.StringUtils.join(pjp.getArgs(),","));
        });
    }


}
