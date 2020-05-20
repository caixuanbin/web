package cn.sessiontech.xcx.controller.common;

import cn.sessiontech.xcx.common.Result;
import cn.sessiontech.xcx.event.GlobalEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xbcai
 * @classname PublicEventCtrl
 * @description 事件发布
 * @date 2019/8/29 21:39
 */
@RestController
@RequestMapping("/event")
public class PublicEventCtrl {
    @Autowired
    ApplicationContext act;
    @GetMapping("/publicEvent/{eventType}")
    public Result publicEvent(@PathVariable("eventType")String eventType){
        act.publishEvent(new GlobalEvent<>(eventType));
        return Result.success();
    }
}
