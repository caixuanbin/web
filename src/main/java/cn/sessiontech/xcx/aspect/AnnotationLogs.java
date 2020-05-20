package cn.sessiontech.xcx.aspect;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnnotationLogs {
    // 描述
    String description() default "";
}
