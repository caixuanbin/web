package cn.sessiontech.xcx.enums.common;

/**
 * @author Administrator
 * @classname FreshRedisEnum
 * @description 刷新缓存
 * @date 2019/8/29 21:48
 */
public enum FreshRedisEnum {
    /**
     *
     */
    FRESH_REDIS_TDICTIONARY("tdc","刷新字典表"),

    FRESH_REDIS_PRODUCTCATEGORY("pcg","产品分类信息")
    ;
    private String code;
    private String name;

    FreshRedisEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
