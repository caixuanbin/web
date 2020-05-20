package cn.sessiontech.xcx.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xbcai
 * @classname ConverterUtils2
 * @description 实体转换类 支持单个和集合转换
 * @date 2019/7/2 21:57
 */
public class ConverterUtils {
    /**
     *
     * @param clazz 转换的目标类对象
     * @param <T> 转换的目标类
     * @return
     */
    private static  <T> T ConverterUtils(Class<T> clazz) {
        try {
            return  clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 单个实体转换
     * @param source 源对象
     * @param clazz 转换的目标对象
     * @return
     */
    public  static <F,T>  T convert(F source,Class<T> clazz){
        T target = ConverterUtils(clazz);
        BeanUtils.copyProperties(source, Objects.requireNonNull(target));
        return target;
    }

    /**
     * 容器实体转换
     * @param sourceList 转换源对象集合
     * @param clazz 转换的目标类对象
     * @return
     */
    public  static <T,F> List<T> convert(List<F> sourceList,Class<T> clazz) {
            return sourceList.stream().map(e-> convert(e,clazz)
            ).collect(Collectors.toList());

    }


}
