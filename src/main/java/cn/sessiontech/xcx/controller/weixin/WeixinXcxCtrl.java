package cn.sessiontech.xcx.controller.weixin;

import cn.sessiontech.xcx.common.Result;
import cn.sessiontech.xcx.dto.weixin.TWeixinUserDTO;
import cn.sessiontech.xcx.dto.weixin.WeixinFormDTO;
import cn.sessiontech.xcx.entity.TWeixinUser;
import cn.sessiontech.xcx.entity.TXcxFormIds;
import cn.sessiontech.xcx.enums.BusinessEnum;
import cn.sessiontech.xcx.enums.ResultCodeEnum;
import cn.sessiontech.xcx.service.TWeixinUserService;
import cn.sessiontech.xcx.service.TXcxFormIdsService;
import cn.sessiontech.xcx.service.WeixinXcxService;
import cn.sessiontech.xcx.utils.ConverterUtils;
import cn.sessiontech.xcx.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @classname WeiXinCtrl
 * @description TODO
 * @date 2019/9/18 21:08
 */
@Slf4j
@RestController
@RequestMapping("/wxXcx")
public class WeixinXcxCtrl {
    @Autowired
    private WeixinXcxService weiXinXcxService;
    @Autowired
    private TWeixinUserService tWeixinUserService;
    @Autowired
    private TXcxFormIdsService tXcxFormIdsService;

    /**
     * 登录凭证校验
     * @param code 临时票据
     */
    @GetMapping("/code2Session")
    public Result code2Session(@RequestParam  String code){
        System.out.println(code);
        return Result.success(weiXinXcxService.login(code));
    }
    @PostMapping("/saveWxLoginUser")
    public Result saveWxLoginUser(@RequestBody @Valid TWeixinUserDTO dto, BindingResult bindingResult, HttpServletRequest req){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        String openid = req.getHeader("openid");
        TWeixinUser winUser = ConverterUtils.convert(dto, TWeixinUser.class);
        winUser.setOpenid(openid);
        ThreadUtils.getOhterThreadPool().execute(()->{
            TWeixinUser u = new TWeixinUser();
            u.setOpenid(openid);
            TWeixinUser entityByParams = tWeixinUserService.findEntityByParams(u);
            if(entityByParams==null){
                tWeixinUserService.saveAndFlush(winUser);
            }
        });
        return Result.success();
    }

    /**
     * 收集表单id用来发送微信模板消息
     * @param dto id集合
     */
    @PostMapping("/saveFormIds")
    public Result saveFormIds(@RequestBody @Valid WeixinFormDTO dto,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        ThreadUtils.getOhterThreadPool().execute(()->{
            List<TXcxFormIds> idsList = new ArrayList<>();
            String[] arrayIds = dto.getIds().split(",");
            for (String formId:arrayIds) {
                TXcxFormIds form = new TXcxFormIds();
                form.setFormId(formId);
                form.setIsValid(BusinessEnum.FORM_ID_EFFECTIVE.getCode());
                idsList.add(form);
            }
            List<TXcxFormIds> tXcxFormIds = tXcxFormIdsService.saveAll(idsList);
            log.info("保存表单id集合如下：{}",tXcxFormIds);
        });

        return Result.success();
    }

    @GetMapping("/getAccessToken")
    public Result getAccessToken(){
        return Result.success(weiXinXcxService.getAccessToken());
    }
}
