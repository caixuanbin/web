package cn.sessiontech.xcx.controller;

import cn.sessiontech.xcx.aspect.AnnotationLogs;
import cn.sessiontech.xcx.common.PageResult;
import cn.sessiontech.xcx.common.QueryObject;
import cn.sessiontech.xcx.common.Result;
import cn.sessiontech.xcx.constant.GlobalConstants;
import cn.sessiontech.xcx.constant.GlobalTimeConstants;
import cn.sessiontech.xcx.controller.base.BaseCtrl;
import cn.sessiontech.xcx.dto.event.PassOnEventDTO;
import cn.sessiontech.xcx.dto.leave.AskForLeaveDTO;
import cn.sessiontech.xcx.dto.leave.AskForLeaveTeacherDTO;
import cn.sessiontech.xcx.dto.leave.TeacherLeaveListQueryDTO;
import cn.sessiontech.xcx.entity.TStudentSchoolTimetable;
import cn.sessiontech.xcx.entity.TSysMessage;
import cn.sessiontech.xcx.entity.TWeixinUser;
import cn.sessiontech.xcx.entity.sys.TSysUser;
import cn.sessiontech.xcx.enums.BusinessEnum;
import cn.sessiontech.xcx.enums.ResultCodeEnum;
import cn.sessiontech.xcx.enums.common.QueryEnum;
import cn.sessiontech.xcx.event.GlobalEvent;
import cn.sessiontech.xcx.properties.WeixinXcxProperties;
import cn.sessiontech.xcx.service.*;
import cn.sessiontech.xcx.service.sys.TSysUserService;
import cn.sessiontech.xcx.utils.DateJdk8Utils;
import cn.sessiontech.xcx.utils.JpaUtils;
import cn.sessiontech.xcx.utils.ThreadUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xbcai
 * @classname TAskForLeaveCtrl
 * @description 请假管理
 * @date 2019/10/5 16:12
 */
@RestController
@RequestMapping("/leave")
public class TAskForLeaveCtrl extends BaseCtrl {
    @Autowired
    private TStudentSchoolTimetableService tStudentSchoolTimetableService;
    @Autowired
    private TSysUserService tSysUserService;
    @Autowired
    private TTeacherLeaveTimeService tTeacherLeaveTimeService;
    @Autowired
    private WeixinXcxService weixinXcxService;
    @Autowired
    private TWeixinUserService tWeixinUserService;
    @Autowired
    private WeixinXcxProperties weixinXcxProperties;
    @Autowired
    private TSysMessageService tSysMessageService;
    @Autowired
    ApplicationContext act;
    @SuppressWarnings("all")
    @AnnotationLogs(description = "学生请假")
    @PostMapping("/askForLeave")
    public Result askForLeave(@RequestBody @Valid AskForLeaveDTO dto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        List<QueryObject<String,Object>> lists = new ArrayList<>();
        lists.add(new QueryObject<>("id",dto.getIds(),QueryEnum.IN));
        List<TStudentSchoolTimetable> byParams = tStudentSchoolTimetableService.findByParams(lists, QueryEnum.QUERY_AND);
        //判断是否有效请假
        Result r = validAskForLeave(byParams);
        if(r!=null){return r;}
        //更新课程表状态
        updateTimeTable(byParams);
        //发送请假订阅信息到公众号
        askLeaveInfo(byParams,BusinessEnum.ASK_LEAVEL_YES.getCode());
        return Result.success();
    }



    @AnnotationLogs(description = "取消学生请假")
    @PostMapping("/cancelLeave")
    public Result cancelLeave(@RequestBody @Valid AskForLeaveDTO dto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        List<TStudentSchoolTimetable> timeTableList = getTimeTableList(dto);
        Result r = cancelLeave(timeTableList);
        if(r!=null){return r;}
        timeTableList.forEach(item->{
            //设置课程状态为已预约
            item.setClassStatus(BusinessEnum.COURSE_STATUS_YYY.getCode());
            item.setUpdateTime(new Date());
            tStudentSchoolTimetableService.saveAndFlush(item);
            PassOnEventDTO cancelEvent = new PassOnEventDTO(GlobalConstants.CANCEL_LEAVE_STUDENT,item.getId());
            //发布学生取消请假事件
            act.publishEvent(new GlobalEvent<>(cancelEvent));
        });

        return Result.success();
    }

    /**
     * 获取课程表列表
     * @param dto 入参
     */
    private List<TStudentSchoolTimetable> getTimeTableList(AskForLeaveDTO dto){
        List<QueryObject<String,Object>> lists = new ArrayList<>();
        lists.add(new QueryObject<>("id",dto.getIds(),QueryEnum.IN));
        List<TStudentSchoolTimetable> byParams = tStudentSchoolTimetableService.findByParams(lists, QueryEnum.QUERY_AND);
        return byParams;
    }

    @SuppressWarnings("all")
    @AnnotationLogs(description = "设置学生旷课")
    @PostMapping("/setTruancy")
    public Result setTruancy(@RequestBody @Valid AskForLeaveDTO dto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        List<QueryObject<String,Object>> lists = new ArrayList<>();
        lists.add(new QueryObject<>("id",dto.getIds(),QueryEnum.IN));
        List<TStudentSchoolTimetable> truancyList = tStudentSchoolTimetableService.findByParams(lists, QueryEnum.QUERY_AND);
        Result r = validCanTruancy(truancyList);
        if(r!=null){return r;}
        truancyList.forEach(item->{
            item.setUpdateTime(new Date());
            //设置状态为也旷课
            item.setClassStatus(BusinessEnum.COURSE_STATUS_KK.getCode());
            tStudentSchoolTimetableService.saveAndFlush(item);
            PassOnEventDTO kkEvent = new PassOnEventDTO(GlobalConstants.SET_STUDENT_KK,item.getId());
            //发布学生旷课事件
            act.publishEvent(new GlobalEvent<>(kkEvent));

        });
        return Result.success();
    }

    @AnnotationLogs(description = "老师请假")
    @PostMapping("/askForLeaveTeacher")
    public Result askForLeaveTeacher(@RequestBody @Valid AskForLeaveTeacherDTO dto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        return Result.success();
    }
    @AnnotationLogs(description = "取消老师请假")
    @PostMapping("/cancelLeaveTeacher")
    public Result cancelLeaveTeacher(@RequestBody @Valid AskForLeaveDTO dto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        return Result.success();
    }

    /**
     * 查询老师请假列表
     * @param dto 查询条件
     * @param bindingResult 校验信息
     */
    @PostMapping("/getTeacherLeaveList")
    public Result getTeacherLeaveList(@RequestBody @Valid TeacherLeaveListQueryDTO dto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        List<QueryObject<String,Object>> lists = new ArrayList<>(10);

        if(StringUtils.isNotEmpty(dto.getBeginTime())){
            lists.add(new QueryObject<>("leaveBeginTime", dto.getBeginTime(), QueryEnum.GREATER_THAN));
        }
        if(StringUtils.isNotEmpty(dto.getEndTime())){
            lists.add(new QueryObject<>("leaveBeginTime",dto.getEndTime(),QueryEnum.LESS_THEN));
        }
        PageResult result = tTeacherLeaveTimeService.findByParams(lists,QueryEnum.QUERY_AND,new JpaUtils().getPageable(dto.getCurrentPage(),dto.getPageSize(), GlobalConstants.DESC,"createTime"));
        return JpaUtils.convertResult(result);

    }

    /**
     * 判断是否有效请假,返回null 便可以请假，不为空便请假非法
     * @param byParams 请假集合
     */
    private Result validAskForLeave(List<TStudentSchoolTimetable> byParams){
        for (TStudentSchoolTimetable timeTable: byParams ) {
            //只有已预约的状态才允许请假
            if(!StringUtils.equals(timeTable.getClassStatus(), BusinessEnum.COURSE_STATUS_YYY.getCode())){
                return Result.fail(ResultCodeEnum.ASK_LEAVE_ILLEGAL,timeTable);
            }
            //如果上课时间已经开始，便不允许请假
            if(DateJdk8Utils.isBeforeNow(timeTable.getClassTime(), GlobalTimeConstants.YYYY_MM_DD_HH_MM)){
                return Result.fail(ResultCodeEnum.ASK_LEAVE_ILLEGAL,timeTable);
            }
        }
        return null;
    }

    /**
     * 判断是否为有效设置旷课 返回null便可以设置旷课，否则便不能设置
     * @param byParams 设置旷课的集合
     */
    private Result validCanTruancy(List<TStudentSchoolTimetable> byParams){
        for (TStudentSchoolTimetable timeTable: byParams ) {
            //只有已预约的状态或已上课才允许设置旷课
            if(!StringUtils.equals(timeTable.getClassStatus(), BusinessEnum.COURSE_STATUS_YYY.getCode())||
                    !StringUtils.equals(timeTable.getClassStatus(), BusinessEnum.COURSE_STATUS_YSK.getCode())){
                return Result.fail(ResultCodeEnum.SET_TRUANCY_ILLEGAL,timeTable);
            }

        }
        return null;
    }


    /**
     * 判断是否可以取消请假，返回null便可以取消，否则便不能取消
     * @param byParams 设置取消请假的集合
     */
    @SuppressWarnings("all")
    private Result cancelLeave(List<TStudentSchoolTimetable> byParams){
        for (TStudentSchoolTimetable timeTable: byParams ) {
            if(!StringUtils.equals(timeTable.getClassStatus(), BusinessEnum.COURSE_STATUS_ASK_LEAVE_BKKS.getCode())&&
                    !StringUtils.equals(timeTable.getClassStatus(), BusinessEnum.COURSE_STATUS_ASK_LEAVE_KKS.getCode())){
                return Result.fail(ResultCodeEnum.CANCEL_LEAVE_ILLEGAL,timeTable);
            }
            Long l = DateJdk8Utils.getToNowMinutes(timeTable.getClassTime(),GlobalTimeConstants.YYYY_MM_DD_HH_MM);
            //如果上课已经开始，不允许取消请假
            if(l<0){
                return Result.fail(ResultCodeEnum.CANCEL_LEAVE_ILLEGAL,timeTable);
            }
            //发布取消请假订阅消息
            askLeaveInfo(byParams,BusinessEnum.ASK_LEAVEL_NO.getCode());
        }
        return null;
    }

    /**
     * 更新课程表
     * @param byParams 请假集合
     */
    private void updateTimeTable(List<TStudentSchoolTimetable> byParams){
        TSysUser user = tSysUserService.findById(byParams.get(0).getTeacherUserId());
        for(TStudentSchoolTimetable timeTable:byParams) {
            if(!StringUtils.equals(timeTable.getTeacherUserId(),user.getId())){
                user = tSysUserService.findById(timeTable.getTeacherUserId());
            }
            //获取到上课时间还有多少分钟
            long toNowMinutes = DateJdk8Utils.getToNowMinutes(timeTable.getClassTime(), GlobalTimeConstants.YYYY_MM_DD_HH_MM);
            //如果在该老师有效请假时间范围内，则是请假-不扣课时，否则是请假-扣课时
            if(toNowMinutes>user.getUserEffectiveLeaveTime()){
                //不扣课时
                timeTable.setClassStatus(BusinessEnum.COURSE_STATUS_ASK_LEAVE_BKKS.getCode());
                PassOnEventDTO dto = new PassOnEventDTO(GlobalConstants.ASK_LEAVE_STUDENT,timeTable.getId());
                //请假不扣课时时，发布该请假请求，后台监听进行生成对应的待安排课程表
                act.publishEvent(new GlobalEvent<>(dto));
            }else{
                //扣课时
                timeTable.setClassStatus(BusinessEnum.COURSE_STATUS_ASK_LEAVE_KKS.getCode());
            }
            timeTable.setUpdateTime(new Date());
            tStudentSchoolTimetableService.saveAndFlush(timeTable);
        }
    }

    /**
     * 发送请假/取消请假订阅信息
     * @param timeTable 请假信息
     * @param flag 1:请假；2：取消请假
     */
    @SuppressWarnings("all")
    private void askLeaveInfo(List<TStudentSchoolTimetable> timeTable,String flag){
        ThreadUtils.getOhterThreadPool().execute(()->{
            String title="请假";
            if(StringUtils.equals(flag,BusinessEnum.ASK_LEAVEL_NO.getCode())){
                title="取消请假";
            }
            //消息列表
            List<TSysMessage> messageList = new ArrayList<>();
            for (TStudentSchoolTimetable time:timeTable) {
                //消息标题
                final String t = "【"+title+"】";
                //发送对应范围内的消息通知，只发给该学生、对应老师、运营者、超级管理员消息
                TSysUser student = tSysUserService.findById(time.getStudentUserId());
                List<QueryObject<String,String>> lists = new ArrayList<>();
                //请假的学生、请假的学生对应的老师，请假的学生的家长 都要发消息
                lists.add(new QueryObject<>("userId",time.getStudentUserId()+","+time.getTeacherUserId()+","+student.getUserParentId(),QueryEnum.IN));
                //超级管理员、运营者都要发消息
                lists.add(new QueryObject<>("userIdentity","super,operator",QueryEnum.IN));
                List<TWeixinUser> all = tWeixinUserService.findByParams(lists,QueryEnum.QUERY_OR);
                all.forEach(item->{
                    //发送微信订阅消息
                    weixinXcxService.sendMessage(weixinXcxProperties.getAskLeave(),item.getOpenid(),time.getStudentUserName()+"--"+t,time.getClassTime(),time.getTeacherUserName()+"的课");
                    TSysMessage message = new TSysMessage();
                    message.setIsRead(BusinessEnum.MESSAGE_NOT_READ.getCode());
                    message.setIsSend(BusinessEnum.MESSAGE_HAD_SEND.getCode());
                    message.setUserId(item.getUserId());
                    message.setUserName(item.getFullName());
                    message.setOpenid(item.getOpenid());
                    message.setSendTime(DateJdk8Utils.getNowTime(GlobalConstants.YYYY_MM_DD_HH_MM_SS));
                    message.setSysContent(time.getStudentUserName()+time.getClassTime()+"节课"+t+";老师："+time.getTeacherUserName());
                    messageList.add(message);
                });
            }
            //消息入库
            tSysMessageService.saveAll(messageList);
        });
    }
    /**
     * 发送请假/取消请假订阅信息
     * @param timeTable 请假信息
     * @param flag 1:请假；2：取消请假
     */
    @SuppressWarnings("all")
    private void askLeaveInfo2(List<TStudentSchoolTimetable> timeTable,String flag){
        ThreadUtils.getOhterThreadPool().execute(()->{
            String title="请假";
            if(StringUtils.equals(flag,BusinessEnum.ASK_LEAVEL_NO.getCode())){
                title="取消请假";
            }
            //消息列表
            List<TSysMessage> messageList = new ArrayList<>();
            for (TStudentSchoolTimetable time:timeTable) {
                //todo  发生短信接口
                //消息标题
                final String t = title;
                //发送对应范围内的消息通知，只发给该学生、对应老师、运营者、超级管理员消息
                TSysUser student = tSysUserService.findById(time.getStudentUserId());
                List<QueryObject<String,String>> lists = new ArrayList<>();
                //请假的学生、请假的学生对应的老师，请假的学生的家长 都要发消息
                lists.add(new QueryObject<>("userId",time.getStudentUserId()+","+time.getTeacherUserId()+","+student.getUserParentId(),QueryEnum.IN));
                //超级管理员、运营者都要发消息
                lists.add(new QueryObject<>("userIdentity","super,operator",QueryEnum.IN));
                List<TWeixinUser> all = tWeixinUserService.findByParams(lists,QueryEnum.QUERY_OR);
                all.forEach(item->{
                    //发送微信订阅消息
                    String content =time.getStudentUserName()+"【"+t+"】"+"，课时:"+time.getClassTime()+"，"+time.getTeacherUserName()+"老师的课";
                    weixinXcxService.sendAskLeaveOrCancelLeave(item.getOpenid(),time.getStudentUserName(),
                            "课时:"+time.getClassTime(),time.getTeacherUserName()+"老师的课");
                    TSysMessage message = new TSysMessage();
                    message.setIsRead(BusinessEnum.MESSAGE_NOT_READ.getCode());
                    message.setIsSend(BusinessEnum.MESSAGE_HAD_SEND.getCode());
                    message.setUserId(item.getUserId());
                    message.setUserName(item.getFullName());
                    message.setOpenid(item.getOpenid());
                    message.setSendTime(DateJdk8Utils.getNowTime(GlobalConstants.YYYY_MM_DD_HH_MM_SS));
                    message.setSysContent(content);
                    messageList.add(message);
                });
            }
            //消息入库
            tSysMessageService.saveAll(messageList);
        });
    }



}
