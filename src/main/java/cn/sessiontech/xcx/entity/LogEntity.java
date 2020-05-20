package cn.sessiontech.xcx.entity;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * @author xbcai
 * @classname LogEntity
 * @description 日志实体类
 * @date 2019/5/21 21:39
 */
@Entity
@Data
public class LogEntity extends BaseEntity {
    private static final long serialVersionUID = 6422462652123335349L;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 方法功能描述
     */
    private String methodDesc;
    /**
     * 入参
     */
    @Lob
    private String params;
    /**
     * 方法返回值
     */
    @Lob
    private String returnValue;
    /**
     * 消耗时间 单位为毫秒
     */
    private long elapsedTime;
    /**
     * 备用字段01
     */
    private String str01;
    /**
     * 备用字段02
     */
    private String str02;
    /**
     * 备用字段03
     */
    private String str03;

}
