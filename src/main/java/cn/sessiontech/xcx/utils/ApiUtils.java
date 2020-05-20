package cn.sessiontech.xcx.utils;

import cn.sessiontech.xcx.constant.GlobalConstants;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import java.util.Map;

/**
 * @author xbcai
 * @classname ApiUtils
 * @description 调用远程接口工具类封装
 * @date 2019/6/11 16:04
 */
@Slf4j
public class ApiUtils {

    /**
     * get 请求
     * @param map 请求参数
     * @param url 请求URL
     * @return
     */
    public static JsonObject getApi(Map<String,String> map,String url){
        HttpRequest httpRequest = HttpRequest.get(url). connectionTimeout(GlobalConstants.CONNECT_TIME_OUT).query(map);
        httpRequest.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpResponse response = httpRequest.send();
        JsonParser jsonParser = new JsonParser();
        JsonElement je = jsonParser.parse(response.bodyText());
        return je.getAsJsonObject();
    }

    /**
     * post 请求
     * @param data 业务数据
     * @param url 请求远程url
     */
    public static JsonObject postApi(Object data,String url){
        String reqBody = JsonUtils.obj2String(data);
        log.info("reqBody:{}",reqBody);
        HttpRequest httpRequest = null;
        HttpResponse response = null;
        httpRequest = HttpRequest.post(url).connectionTimeout(GlobalConstants.CONNECT_TIME_OUT).bodyText(reqBody);
        httpRequest.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response = httpRequest.send();
        log.info("request header: \n{}", httpRequest.toString(false));
        log.info("request body text: \n{}", httpRequest.bodyText());
        log.info("--response header \n {}", response.toString(false));
        log.info("response body text \n {}", response.bodyText());
        JsonParser jsonParser = new JsonParser();
        JsonElement je = jsonParser.parse(response.bodyText());
        return je.getAsJsonObject();
    }





    public static void main(String[] args) {

    }
}
