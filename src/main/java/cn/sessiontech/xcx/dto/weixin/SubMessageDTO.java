package cn.sessiontech.xcx.dto.weixin;

import lombok.Data;

/**
 * @author xbcai
 * @classname SubMessageDTO
 * @description 发送订阅消息DTO
 * @date 2019/10/21 22:36
 */
@Data
public class SubMessageDTO {
    /**
     * 接收者（用户）的 openid
     */
    private String touser;
    /**
     * 所需下发的订阅模板id
     */
    private String template_id;
    /**
     * 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
     */
    private String page;
    /**
     * 模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } }
     */
    private Object data;
}
