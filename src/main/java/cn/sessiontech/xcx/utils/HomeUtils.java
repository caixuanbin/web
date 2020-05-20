package cn.sessiontech.xcx.utils;

import cn.sessiontech.xcx.common.QueryObject;
import cn.sessiontech.xcx.enums.common.QueryEnum;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xbcai
 * @classname HomeUtils
 * @description 到家服务业务公共类
 * @date 2019/7/16 15:24
 */
public class HomeUtils {
    /**
     * 匹配函数方法,针对需要改变属性查询的字段重新设置查询方式
     * @param lists 要进行处理的集合
     * @param attr 要匹配的属性，和数据库字段的属性要保持一致
     * @param queryEnum 要对匹配的属性做何种查询
     * @return 返回过滤后的函数
     */
    public static List<QueryObject<String,Object>> functionMatch(List<QueryObject<String,Object>> lists, String attr, QueryEnum queryEnum     ){
        return  lists.stream().map(item->{
            if(StringUtils.equals(item.key,attr)){
                item.setQe(queryEnum);
            }
            return item;
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        System.out.println(LocalDate.parse("2018-08-29 12:12:12",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

}
