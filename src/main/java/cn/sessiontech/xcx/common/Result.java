package cn.sessiontech.xcx.common;

import cn.sessiontech.xcx.enums.ResultCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xbcai
 * @classname Result
 * @description controller 返回数据层封装
 * @date 2019/5/21 8:42
 */
@Data
public class Result implements Serializable {

    private static final long serialVersionUID = 656089094643423809L;
    private int code;
    private String message;
    private Object data;

    private Result(ResultCodeEnum resultCode, Object data) {
        this.code = resultCode.code;
        this.message = resultCode.message;
        this.data = data;
    }

    private  Result(ResultCodeEnum resultCode) {
        this.code = resultCode.code;
        this.message = resultCode.message;
    }

    public Result() {
    }

    /**
     * 返回成功
     * @return
     */
    public static Result success() {
        return new Result(ResultCodeEnum.SUCCESS);
    }

    /**
     * 返回成功
     * @param data
     * @return
     */
    public static Result success(Object data){
        return new Result(ResultCodeEnum.SUCCESS,data);
    }

    /**
     * 调用失败返回
     * @param resultCode
     * @return
     */
    public static Result fail(ResultCodeEnum resultCode){
        return new Result(resultCode);
    }

    /**
     * 调用失败返回
     * @param resultCode
     * @param data
     * @return
     */
    public static Result fail(ResultCodeEnum resultCode, Object data){
        return new Result(resultCode,data);
    }
}
