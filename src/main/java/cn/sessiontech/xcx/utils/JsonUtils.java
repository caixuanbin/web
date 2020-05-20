package cn.sessiontech.xcx.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author xbcai
 * @classname JsonUtils
 * @description 封装Jackson的工具类
 * @date 2019/5/14 21:20
 */
@Slf4j
public class JsonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        //忽略在json字符串中存在，但在java对象中不存在对应属性的情况，防止出错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    /**
     * 将对象转为json字符串输出
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2String(T obj){
        if(obj==null){
            return null;
        }
        try {
            return obj instanceof String ?(String) obj:objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("对象转json字符串解析错误",e);
            return null;
        }
    }

    /**
     * 将对象转为json，并格式化的输出
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2StringPretty(T obj){
        if(obj==null){
            return null;
        }
        try {
            return obj instanceof String ?(String) obj:objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("对象转json字符串解析错误",e);
            return null;
        }
    }

    /**
     * 将json字符串转为java对象（适用单个对象使用，返回值就是单个对象）
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str,Class<T> clazz){
        if(StringUtils.isEmpty(str)||clazz==null){
            return null;
        }
        try {
            return clazz.equals(String.class)?(T)str:objectMapper.readValue(str,clazz);
        } catch (IOException e) {
            log.warn("json字符串转对象解析错误",e);
            return null;
        }
    }

    /**
     * 将json字符串转为java对象 （这里适用容器存储多个对象的时候使用，返回值是容器对象例如：List<Object></>）
     * @param str
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, TypeReference<T> typeReference){
        if(StringUtils.isEmpty(str)||typeReference==null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class)?str:objectMapper.readValue(str,typeReference));
        } catch (IOException e) {
            log.warn("json字符串转对象解析错误",e);
            return null;
        }
    }




}
