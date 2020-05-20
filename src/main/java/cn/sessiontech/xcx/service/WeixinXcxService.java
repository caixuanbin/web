package cn.sessiontech.xcx.service;

import cn.sessiontech.xcx.common.Result;
import cn.sessiontech.xcx.dto.weixin.SubMessageDTO;
import cn.sessiontech.xcx.vo.weixin.WeixinXcxLoginVO;

/**
 * @author xbcai
 * @classname WeiXinXcxService
 * @description 小程序公共服务类
 * @date 2019/9/18 21:13
 */
public interface WeixinXcxService {
    /**
     * 登录授权
     * @param code 临时code
     * @return 返回登录小程序返回的实体信息
     */
    WeixinXcxLoginVO login(String code);

    Result sendInfo(SubMessageDTO dto,String accessToken);

    /**
     * 发送预约消息
     * @param templateId 模板id
     * @param openid 发送对象
     * @param content 发送内容
     * @param time 课程时间
     * @param remarks 备注
     */
    void sendMessage(String templateId,String openid,String content,String time,String remarks);

    /**
     * 请假或取消请假
     * @param openid 微信openid
     * @param studentName 请假人
     * @param content 请假内容
     * @param remarks 备注
     */
    void sendAskLeaveOrCancelLeave(String openid, String studentName,String content,String remarks);

    String getAccessToken();


}
