package cn.sessiontech.xcx.controller.weixin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xbcai
 * @classname WeiXinCheckMsgCtrl
 * @description 微信小程序消息推送设置
 * @date 2019/9/15 17:35
 */
@Slf4j
@RestController
@RequestMapping("/wechat")
public class WeixinCheckMsgCtrl {
    @RequestMapping
    public String checkWxMsg(String signature,String timestamp,String nonce,String echostr){
        log.info("微信服务器发过来的信息signature：{}，timestamp：{}，nonce：{}，echostr：{}",signature,timestamp,nonce,echostr);
        return echostr;
    }
}
