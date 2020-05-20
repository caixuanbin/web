package cn.sessiontech.xcx.utils.weixin.mp.xcx;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @classname WeixinXcxApiUtils
 * @description 微信工具类
 * @date 2019/9/18 21:57
 */
public class WeixinXcxApiUtils {
    /**
     * 构建微信模板data里面的map键值对
     * @param value map 里面的值
     * @return 返回构造好的map键值对
     */
    private static Map<String,String> setValue(String value){
        Map<String,String> map = new HashMap<>(1);
        map.put("value",value);
        return map;
    }

    /**
     * 构造微信订阅模板内容data部分
     * @param map 要构建的data部分集合
     * @param key 要往map集合里面放的key
     * @param value 微信模板对应值
     *例子：
     *              {
     *   "touser": "OPENID",
     *   "template_id": "TEMPLATE_ID",
     *   "page": "index",
     *   "data": {
     *       "number01": {
     *           "value": "339208499"
     *       },
     *       "date01": {
     *           "value": "2015年01月05日"
     *       },
     *       "site01": {
     *           "value": "粤海喜来登酒店"
     *       } ,
     *       "site02": {
     *           "value": "广州市天河区天河路208号"
     *       }
     *   }
     * }
     *
     */
    public static void putValue(Map<String,Map<String,String>> map,String key,String value){
        map.put(key,setValue(value));
    }

}
