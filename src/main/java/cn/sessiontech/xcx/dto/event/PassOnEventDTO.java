package cn.sessiontech.xcx.dto.event;

import lombok.Data;

/**
 * @author xbcai
 * @classname PassOnEventDTO
 * @description 传递事件实体
 * @date 2019/10/5 18:17
 */
@Data
public class PassOnEventDTO {
    public PassOnEventDTO(String eventType, String key) {
        this.eventType = eventType;
        this.key = key;
    }

    /**
     * 事件类型
     */
    private String eventType;
    /**
     * key
     */
    private String key;
}
