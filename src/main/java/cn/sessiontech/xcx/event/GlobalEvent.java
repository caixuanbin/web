package cn.sessiontech.xcx.event;


import org.springframework.context.ApplicationEvent;

/**
 * @author xbcai
 * @classname GlobalEvent
 * @description 全局性事件
 * @date 2019/8/29 21:13
 */
public class GlobalEvent<T> extends ApplicationEvent {
    public GlobalEvent(T source) {
        super(source);
    }
}
