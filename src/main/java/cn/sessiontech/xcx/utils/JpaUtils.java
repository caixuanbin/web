package cn.sessiontech.xcx.utils;

import cn.sessiontech.xcx.common.PageResult;
import cn.sessiontech.xcx.controller.base.BaseCtrl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author xbcai
 * @classname JpaUtils
 * @description jpa封装的工具类，不操作数据库
 * @date 2019/6/19 9:14
 */
public class JpaUtils extends BaseCtrl {
    /**
     * 封装分页返回
     * @param currentPage 当前页
     * @param pageSize 一页显示几条数据
     * @param direction 排序方式
     * @param properties 排序字段
     * @return
     */
    public Pageable getPageable(Integer currentPage, Integer pageSize, String direction, String ...properties){
        Sort sort = new Sort(Sort.Direction.fromString(direction),properties);
        Pageable pageable = this.initPage(currentPage,pageSize,sort);
        return pageable;
    }

    /**
     * 转换行业的格外为自己定义的格式
     * @param result 分页数据
     * @return 返回自己定义好的格式
     */
    public static PageResult convertResult(PageResult result){
        if(result.getCode()==0){
            result.setCode(200);
        }
        return result;
    }

}
