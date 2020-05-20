package cn.sessiontech.xcx.enums.common;

/**
 * @author xbcai
 * @classname QueryEnum
 * @description 构建查询参数
 * @date 2019/6/22 14:31
 */
public enum QueryEnum {
    /**
     * 等于
     */
    EQUAL,
    /**
     * 不等
     */
    NOT_EQUAL,
    /**
     * 大于
     */
    GREATER_THAN,
    /**
     * 小于
     */
    LESS_THEN,
    /**
     * 范围
     */
    IN,
    /**
     * 左边模糊查询 %v
     */
    LEFT_LIEK,
    /**
     * 右边模糊查询 v%
     */
    RIGHT_LIKE,
    /**
     * 全模糊查询 %v%
     */
    ALL_LIKE,
    /**
     * 不匹配 not like '%v%'
     */
    NOT_LIKE,
    /**
     * AND 查询的方式
     */
    QUERY_AND,
    /**
     * OR 查询的方式
     */
    QUERY_OR,

}
