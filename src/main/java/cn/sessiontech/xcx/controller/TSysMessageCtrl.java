package cn.sessiontech.xcx.controller;

import cn.sessiontech.xcx.common.PageResult;
import cn.sessiontech.xcx.common.QueryObject;
import cn.sessiontech.xcx.common.Result;
import cn.sessiontech.xcx.constant.GlobalConstants;
import cn.sessiontech.xcx.controller.base.BaseCtrl;
import cn.sessiontech.xcx.dto.message.MessageListQueryDTO;
import cn.sessiontech.xcx.enums.RedisEnum;
import cn.sessiontech.xcx.enums.ResultCodeEnum;
import cn.sessiontech.xcx.enums.common.QueryEnum;
import cn.sessiontech.xcx.service.TSysMessageService;
import cn.sessiontech.xcx.utils.DateUtils;
import cn.sessiontech.xcx.utils.JpaUtils;
import cn.sessiontech.xcx.utils.RedisKeyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xbcai
 * @classname TSysMessageCtrl
 * @description 消息管理
 * @date 2019/10/5 0:00
 */
@RestController
@RequestMapping("/message")
public class TSysMessageCtrl extends BaseCtrl {
    @Autowired
    private TSysMessageService tSysMessageService;
    @Autowired
    private RedisKeyUtils redisKeyUtils;

    /**
     * 查询消息列表
     * @param dto 查询条件
     * @param bindingResult 校验信息
     */
    @SuppressWarnings("all")
    @PostMapping("/getMessageList")
    public Result getMessageList(@RequestBody @Valid MessageListQueryDTO dto, BindingResult bindingResult, HttpServletRequest req){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        String openid = req.getHeader("openid");
        List<String> id = redisKeyUtils.hmget(RedisEnum.HOME_SYS_USER.getCode() + openid, "id");
        Map<String,String> sysUser = (Map<String,String>)req.getSession().getAttribute(RedisEnum.LOGIN_SESSION_USER.getCode());
        List<QueryObject<String,Object>> lists = new ArrayList<>(5);
        if(StringUtils.isNotEmpty(dto.getKeyword())){
            lists.add(new QueryObject<>("userName",dto.getKeyword()));
        }
       lists.add(new QueryObject<>("userId",id.get(0)));
        if(StringUtils.isNotEmpty(dto.getBeginTime())){
            lists.add(new QueryObject<>("createTime", DateUtils.formatString(dto.getBeginTime()+GlobalConstants.START_TIME_SUFFIX,GlobalConstants.YYYY_MM_DD_HH_MM_SS), QueryEnum.GREATER_THAN));
        }
        if(StringUtils.isNotEmpty(dto.getEndTime())){
            lists.add(new QueryObject<>("createTime",DateUtils.formatString(dto.getEndTime()+GlobalConstants.END_TIME_SUFFIX,GlobalConstants.YYYY_MM_DD_HH_MM_SS),QueryEnum.LESS_THEN));
        }
        PageResult result = tSysMessageService.findByParams(lists,QueryEnum.QUERY_AND,
                new JpaUtils().getPageable(dto.getCurrentPage(),dto.getPageSize(), GlobalConstants.DESC,"createTime"));
        return JpaUtils.convertResult(result);
    }
}
