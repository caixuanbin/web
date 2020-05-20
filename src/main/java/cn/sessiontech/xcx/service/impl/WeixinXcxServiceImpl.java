package cn.sessiontech.xcx.service.impl;

import cn.sessiontech.xcx.common.Result;
import cn.sessiontech.xcx.constant.WeixinXcxConstants;
import cn.sessiontech.xcx.dto.weixin.SubMessageDTO;
import cn.sessiontech.xcx.enums.WxEnum;
import cn.sessiontech.xcx.properties.WeixinXcxProperties;
import cn.sessiontech.xcx.service.WeixinXcxService;
import cn.sessiontech.xcx.utils.ApiUtils;
import cn.sessiontech.xcx.utils.JsonUtils;
import cn.sessiontech.xcx.utils.RedisKeyUtils;
import cn.sessiontech.xcx.utils.weixin.mp.xcx.WeixinXcxApiUtils;
import cn.sessiontech.xcx.vo.weixin.WeixinXcxLoginVO;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Administrator
 * @classname WeixinXcxServiceImpl
 * @description 小程序服务类
 * @date 2019/9/18 21:14
 */
@Service
@Slf4j
public class WeixinXcxServiceImpl implements WeixinXcxService {
    @Autowired
    private WeixinXcxProperties weixinXcxProperties;
    @Autowired
    private RedisKeyUtils redisKeyUtils;
    @Override
    public WeixinXcxLoginVO login(String code) {
        Map<String,String> map= new HashMap<>(5);
        map.put("appid",weixinXcxProperties.getAppid());
        map.put("secret",weixinXcxProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type",weixinXcxProperties.getGrantType());
        JsonObject api = ApiUtils.getApi(map, WeixinXcxConstants.JSCODE2SESSION);
        return JsonUtils.string2Obj(api.toString(),WeixinXcxLoginVO.class);
    }

    @Override
    public Result sendInfo(SubMessageDTO dto,String accessToken) {
        String url = WeixinXcxConstants.SUB_MESSAGE_URL.replace("#token", accessToken);
        ApiUtils.postApi(dto, url);
        return Result.success();
    }

    @Override
    public void sendMessage(String templateId, String openid, String content, String time, String remarks) {
        SubMessageDTO dto = new SubMessageDTO();
        dto.setTemplate_id(templateId);
        dto.setPage("");
        dto.setTouser(openid);
        Map<String, Map<String,String>> map = new TreeMap<>();
        WeixinXcxApiUtils.putValue(map,"thing1",content);
        WeixinXcxApiUtils.putValue(map,"time2",time);
        WeixinXcxApiUtils.putValue(map,"thing3",remarks);
        dto.setData(map);
        String url = WeixinXcxConstants.SUB_MESSAGE_URL.replace("#token", getAccessToken());
        ApiUtils.postApi(dto, url);
    }

    @Override
    public void sendAskLeaveOrCancelLeave(String openid, String studentName,String content,String remarks) {
        SubMessageDTO dto = new SubMessageDTO();
        dto.setTemplate_id(weixinXcxProperties.getAskLeave());
        dto.setPage("");
        dto.setTouser(openid);
        Map<String, Map<String,String>> map = new TreeMap<>();
        WeixinXcxApiUtils.putValue(map,"name4",studentName);
        WeixinXcxApiUtils.putValue(map,"thing2",content);
        WeixinXcxApiUtils.putValue(map,"thing3",remarks);
        WeixinXcxApiUtils.putValue(map,"phrase1","通过");
        dto.setData(map);
        String url = WeixinXcxConstants.SUB_MESSAGE_URL.replace("#token", getAccessToken());
        ApiUtils.postApi(dto, url);
    }

    @Override
    public  String getAccessToken(){
        //先从缓存获取，如果缓存没有，再请求微信后台获取然后存到缓存
        String s = redisKeyUtils.get(WxEnum.WX_ACCESS_TOKEN.getKey());
        if(StringUtils.isEmpty(s)){
            Map<String,String> map = new HashMap<>(5);
            map.put("appid",weixinXcxProperties.getAppid());
            map.put("secret",weixinXcxProperties.getSecret());
            map.put("grant_type","client_credential");
            JsonObject api = ApiUtils.getApi(map, WeixinXcxConstants.ACESS_TOKEN_URL);
            String accessToken = api.getAsJsonObject().get("access_token").getAsString();
            redisKeyUtils.setex(WxEnum.WX_ACCESS_TOKEN.getKey(),WeixinXcxConstants.EXPIRE_TIME,accessToken);
            return accessToken;
        }else{
            return s;
        }
    }



    /**
     * 交易时间
     * {{keyword1.DATA}}
     *
     * 交易类型
     * {{keyword2.DATA}}
     *
     * 商户详情
     * {{keyword3.DATA}}
     * @param args
     */
    public static void main(String[] args) {
        String token="27_gj9V61qIm_M3tfyrOZPKfTtLQBGX9sPdm7aonvaZm3Kcsdlco_dYNcS5CPSZoyYy65N_rGPVTPzg4ZWyD-eo104wqfu-4v9AHF3X8j8T9Bo-OQES2L0RZT4XZONsz37WIt9dTWUD4IvO6DOQNQUfABADGK";
        SubMessageDTO dto = new SubMessageDTO();
        dto.setTemplate_id("MbbHCdlx52bTY-bjmHtrPCn1nFnDtfWRW2sSqKe_UiQ");
        dto.setPage("");
        dto.setTouser("odSee4lNWenyyA_Ri5T9OHJOSFgo");
        Map<String, Map<String,String>> map = new TreeMap<>();
//        WeixinXcxApiUtils.putValue(map,"name4","张三--请假");
//        WeixinXcxApiUtils.putValue(map,"thing2","课时：2019-11-19 20:30");
//        WeixinXcxApiUtils.putValue(map,"thing3","陈老师的课");
//        WeixinXcxApiUtils.putValue(map,"phrase1","通过");

        WeixinXcxApiUtils.putValue(map,"thing1","张三--【请假】");
        WeixinXcxApiUtils.putValue(map,"time2","2019年10月1日 13:00");
        WeixinXcxApiUtils.putValue(map,"thing3","陈老师的课");
        dto.setData(map);
        String url = WeixinXcxConstants.SUB_MESSAGE_URL.replace("#token", token);
        ApiUtils.postApi(dto, url);


    }
}
