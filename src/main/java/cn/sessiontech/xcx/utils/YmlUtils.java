package cn.sessiontech.xcx.utils;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xbcai
 * @classname YmlUtils
 * @description 读取环境配置信息
 * @date 2019/6/6 23:00
 */
public class YmlUtils {
    private static String base_file = "application.yml";

    private static String prex_file = "application-";

    private static String suffix  = ".yml";

    private final static String active_key = "spring.profiles.active";

    private static Map<String,String> baseResult = new HashMap<>();

    static {
        initSystem(base_file);
    }

    /**
     * 初始化环境配置信息
     * @return
     */
    private static void initSystem(String file){
        baseResult = new HashMap<>();
        if(file == null) {
            file = base_file;
        }
        InputStream in = YmlUtils.class.getClassLoader().getResourceAsStream(file);
        Yaml props = new Yaml();
        Object obj = props.loadAs(in,Map.class);
        Map<String,Object> param = (Map<String, Object>) obj;
        for(Map.Entry<String,Object> entry:param.entrySet()){
            String key = entry.getKey();
            Object val = entry.getValue();

            if(val instanceof Map){
                forEachYaml(key,(Map<String, Object>) val);
            }else{
                baseResult.put(key,val.toString());
            }
        }
        //如果是 application.yml 便读取其中的激活环境后缀再重新读取该激化的环境配置信息
        if(StringUtils.equals(base_file,file)){
            String dev = baseResult.get(active_key);
            initSystem(prex_file+dev+suffix);
        }
    }

    /**
     * 根据key获取值
     * @param key
     * @return
     */
    public static String getValue(String key){
        return baseResult.get(key);
    }

    /**
     * 遍历yml文件，获取map集合
     * @param key_str
     * @param obj
     * @return
     */
    private static Map<String,String> forEachYaml(String key_str,Map<String, Object> obj){
        for(Map.Entry<String,Object> entry:obj.entrySet()){
            String key = entry.getKey();
            Object val = entry.getValue();

            String str_new = "";
            if(StringUtils.isNotEmpty(key_str)){
                str_new = key_str+ "."+key;
            }else{
                str_new = key;
            }
            if(val instanceof Map){
                forEachYaml(str_new,(Map<String, Object>) val);
            }else{
                baseResult.put(str_new,val.toString());
            }
        }
        return baseResult;
    }

    public static void main(String[] args) {
        System.out.println(getValue("wechat.appsecret"));
    }

}
