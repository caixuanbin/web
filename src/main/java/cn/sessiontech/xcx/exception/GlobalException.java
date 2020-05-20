package cn.sessiontech.xcx.exception;

import cn.sessiontech.xcx.common.Result;
import cn.sessiontech.xcx.enums.ResultCodeEnum;
import cn.sessiontech.xcx.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xbcai
 * @classname GlobalException
 * @description 全局异常 捕获抛出的异常
 * @date 2019/5/21 9:33
 */
@RestController
@ControllerAdvice
@Slf4j
public class GlobalException {
    @ExceptionHandler(value = {java.lang.NullPointerException.class})
    public Result nullPointException(Exception e){
        log.info("空指针异常:{}",JsonUtils.obj2String(e.getMessage()));
        return Result.fail(ResultCodeEnum.EXPECTATION_FAILED,e.getMessage());
    }
    @ExceptionHandler(value = {java.lang.ArithmeticException.class})
    public Result zeroException(Exception e){
        log.info("算术异常:{}",JsonUtils.obj2String(Result.fail(ResultCodeEnum.EXPECTATION_FAILED,e.getMessage())));
        return Result.fail(ResultCodeEnum.EXPECTATION_FAILED,e.getMessage());
    }
    @ExceptionHandler(value = {java.lang.RuntimeException.class})
    public Result runtimeException(Exception e){
        log.info("运行时异常：{}"+JsonUtils.obj2String(e.getMessage()));
        return Result.fail(ResultCodeEnum.EXPECTATION_FAILED,e.getMessage());
    }

    @ExceptionHandler(value = {java.lang.Exception.class})
    public Result otherException(Exception e){
        log.info("非运行时异常：{}"+JsonUtils.obj2String(e.getMessage()));
        return Result.fail(ResultCodeEnum.EXPECTATION_FAILED,e.getMessage());
    }

    @ExceptionHandler(value = {java.lang.InterruptedException.class})
    public Result interruptedException(Exception e){
        log.info("非运行时异常：{}"+JsonUtils.obj2String(e.getMessage()));
        return Result.fail(ResultCodeEnum.EXPECTATION_FAILED,e.getMessage());
    }

    @ExceptionHandler(value = {java.util.concurrent.ExecutionException.class})
    public Result executionException(Exception e){
        log.info("非运行时异常：{}"+JsonUtils.obj2String(e.getMessage()));
        return Result.fail(ResultCodeEnum.EXPECTATION_FAILED,e.getMessage());
    }




}
