package cn.sessiontech.xcx.controller.common;


import cn.sessiontech.xcx.config.ProjectUrlConfig;
import cn.sessiontech.xcx.entity.TWeixinUser;
import cn.sessiontech.xcx.enums.WxEnum;
import cn.sessiontech.xcx.service.TWeixinUserService;
import cn.sessiontech.xcx.utils.JsonUtils;
import cn.sessiontech.xcx.utils.RedisKeyUtils;
import cn.sessiontech.xcx.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.http.URIUtil;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * @author xbcai
 * @classname WxCtrl
 * @description 微信服务层
 * @date 2019/3/19 9:43
 */
@Controller
@CrossOrigin(origins ="*")
@Slf4j
@RequestMapping("/api/wx")
public class WxCtrl {
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private ProjectUrlConfig projectUrlConfig;
    @Autowired
    private TWeixinUserService tWeixinUserService;
    @Autowired
    private RedisKeyUtils redisKeyUtils;

    /**
     * 获取微信授权链接，返回到前端由前端发起授权请求（暂时没用到）
     * @param redirectURL 微信重定向的地址
     * @return
     */
    @GetMapping("/authorization")
    @ResponseBody
    public String authorization(@RequestParam("redirectURL") String redirectURL){
        String wxURL = wxMpService.oauth2buildAuthorizationUrl(redirectURL,WxConsts.OAuth2Scope.SNSAPI_BASE, URLEncoder.encode("saas"));
        log.info("【微信网页授权】获取code,redirectUrl={}",redirectURL);
        return wxURL;
    }

    /**
     *
     * 微信签名
     * @param url
     * @return
     * @throws Exception
     */
    @GetMapping("/sign")
    @ResponseBody
    public WxJsapiSignature createJsapiSignature(@RequestParam("url") String url) throws Exception {
        log.info("微信授权：{}",url);
        WxJsapiSignature sign = wxMpService.createJsapiSignature(url);
        log.info("签名:{}",sign);
        return sign;
    }

    /**
     * 微信授权（暂时没用到）
     * @param state
     * @param request
     * @param response
     */
    @GetMapping("/auth")
    public void auth(@RequestParam("state") String state, HttpServletRequest request, HttpServletResponse response){
        String url=projectUrlConfig.getWechatMpAuthorize()+"/htsaas/api/wx/getOpenid";
        String redirectUrl=wxMpService.oauth2buildAuthorizationUrl(url,
                WxConsts.OAuth2Scope.SNSAPI_BASE, URLEncoder.encode(state));
        log.info("【微信网页授权】获取code,redirectUrl={}",redirectUrl);
        try {
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("异常消息："+e.getMessage());
        }
    }

    /**
     * 获取微信openid
     * @param code
     * @param returnUrl
     * @return
     */
    @GetMapping("/getOpenid")
    @ResponseBody
    public String getOpenid(@RequestParam("code") String code,
                            @RequestParam("state") String returnUrl){
         WxMpOAuth2AccessToken wxMpOAuth2AccessToken=new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken=wxMpService.oauth2getAccessToken(code);
            final WxMpOAuth2AccessToken token = wxMpOAuth2AccessToken;
            ThreadUtils.getLogThreadPool().execute(()->{
                try {
                    WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(token,"zh_CN");
                    saveWeixinUser(wxMpUser);
                } catch (WxErrorException e) {
                    e.printStackTrace();
                }
            });
            log.info(wxMpOAuth2AccessToken.getAccessToken());
        }catch (WxErrorException e){
            log.error("【微信网页授权】,{}",e);
        }
        String openId=wxMpOAuth2AccessToken.getOpenId();
        log.info("【微信网页授权】获取openid="+openId+"url={}",wxMpOAuth2AccessToken);
        return openId;

    }

    /**
     * 保存微信用户信息
     * @param wxMpUser 微信用户基本信息
     */
    public void saveWeixinUser(WxMpUser wxMpUser){
        if(wxMpUser!=null){
            TWeixinUser user = new TWeixinUser();
            user.setOpenid(wxMpUser.getOpenId());
            user = tWeixinUserService.findEntityByParams(user);
            if(user==null){
                user = new TWeixinUser();
                user.setOpenid(wxMpUser.getOpenId());
                user.setCity(wxMpUser.getCity());
                user.setCountry(wxMpUser.getCountry());
                user.setAvatarUrl(wxMpUser.getHeadImgUrl());
                user.setProvince(wxMpUser.getProvince());
                user.setGender(wxMpUser.getSex()+"");
                user.setUnionid(wxMpUser.getUnionId());
                user.setNickName(wxMpUser.getNickname());
                user.setPrivilege(JsonUtils.obj2String(wxMpUser.getPrivileges()));
                user.setCreateTime(new Date());
                user.setUpdateTime(new Date());
                //将微信头像设置进缓存
                redisKeyUtils.set(WxEnum.WX_USER_HEADIMGURL.getKey()+wxMpUser.getOpenId(),wxMpUser.getHeadImgUrl());
                tWeixinUserService.saveAndFlush(user);
            }
        }
    }

    /**
     * ///////////////////////////////20190602/////////////////////////////////////////
     */

    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl){
        //2.调用方法
        String url=projectUrlConfig.getWechatMpAuthorize()+"/htsaas/api/wx/userInfo";
        String redirectUrl=wxMpService.oauth2buildAuthorizationUrl(url,WxConsts.OAuth2Scope.SNSAPI_BASE, URLEncoder.encode(returnUrl));
        log.info("【微信网页授权】获取code,redirectUrl={}",redirectUrl);
        //重定向到下面一个方法
        return "redirect:"+redirectUrl;
    }
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) throws WxErrorException{
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken=new WxMpOAuth2AccessToken();
        wxMpOAuth2AccessToken=wxMpService.oauth2getAccessToken(code);
        String openId=wxMpOAuth2AccessToken.getOpenId();
        log.info("【微信网页授权】获取openid,returnUrl={}",returnUrl);
        return "redirect:"+ returnUrl+"?openid="+openId;

    }//以上两个方法是SDK方式微信网页授权的过程，
    //访问http://sqmax.natapp1.cc/sell/wechat/authorize?returnUrl=http://www.imooc.com，
    //最终将会跳转到这个链接：http://www.imooc.com?openid={openid}

    public static void main(String[] args) {
        String str = URIUtil.encodeURIComponent("http://114saas.sessiontech.cn/114shop/#/admin/login");
        System.out.println(str);
    }
}
