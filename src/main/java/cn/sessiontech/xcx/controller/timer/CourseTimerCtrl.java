package cn.sessiontech.xcx.controller.timer;

import cn.sessiontech.xcx.common.QueryObject;
import cn.sessiontech.xcx.constant.GlobalConstants;
import cn.sessiontech.xcx.constant.GlobalTimeConstants;
import cn.sessiontech.xcx.entity.*;
import cn.sessiontech.xcx.enums.BusinessEnum;
import cn.sessiontech.xcx.enums.common.QueryEnum;
import cn.sessiontech.xcx.properties.WeixinXcxProperties;
import cn.sessiontech.xcx.service.*;
import cn.sessiontech.xcx.utils.DateJdk8Utils;
import cn.sessiontech.xcx.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * @classname CourseTimerCtrl
 * @description 定时任务
 * @date 2019/10/6 19:08
 */
@RestController
@Slf4j
public class CourseTimerCtrl {
    @Autowired
    private TStudentSchoolTimetableService tStudentSchoolTimetableService;
    @Autowired
    private TClassOrderService tClassOrderService;
    @Autowired
    private TStudentCourseTobeArrangedService tStudentCourseTobeArrangedService;
    @Autowired
    private WeixinXcxService weixinXcxService;
    @Autowired
    private WeixinXcxProperties weixinXcxProperties;
    @Autowired
    private TWeixinUserService tWeixinUserService;
    @Autowired
    private TSysMessageService tSysMessageService;

    @Scheduled(cron="0 0/5 * * * ?")
    public void courseRelevant(){
        log.info("更新课程相关的定时任务开始----------");
        //更新待安排课程表
        ThreadUtils.getOhterThreadPool().execute(this::updateTobeArrange);
        //更新课程表，将已经上课的课程插入到课程工单表
        ThreadUtils.getOhterThreadPool().execute(this::updateTimeTable);
        //上课提醒，发送提醒消息
        ThreadUtils.getOhterThreadPool().execute(this::remindClass);
    }

    public static void main(String[] args) {
        long t = DateJdk8Utils.getToNowMinutes("2019-10-22 02:00", GlobalTimeConstants.YYYY_MM_DD_HH_MM);
        long now = t+GlobalConstants.classTimeLenght;
        System.out.println(t);
        System.out.println(now);
        String sixHours = DateJdk8Utils.minus(-6, ChronoUnit.HOURS, GlobalTimeConstants.YYYY_MM_DD_HH_MM);
        System.out.println(sixHours);
        String halfHour = DateJdk8Utils.minus(-30, ChronoUnit.MINUTES, GlobalTimeConstants.YYYY_MM_DD_HH_MM);
        System.out.println(halfHour);
        String nowTime = DateJdk8Utils.getNowTime(GlobalTimeConstants.YYYY_MM_DD_HH_MM);
        System.out.println(nowTime);
    }
    /**
     * 上课提醒
     * 离上课时间6小时提醒一次，离上课时间30分钟提醒一次
     */
    private void remindClass(){
        // 大于现在6个小时的时间
        String sixHours = DateJdk8Utils.minus(-6, ChronoUnit.HOURS, GlobalTimeConstants.YYYY_MM_DD_HH_MM);
        //大于现在30分钟的时间
        String halfHour = DateJdk8Utils.minus(-30, ChronoUnit.MINUTES, GlobalTimeConstants.YYYY_MM_DD_HH_MM);
        List<QueryObject<String,String>> lists = new ArrayList<>();
        //查还未发送消息提醒的
        lists.add(new QueryObject<>("isSendClass",BusinessEnum.GO_TO_CLASS_HAD_NOT_SEND_MESSAGE.getCode()));
        //有效的数据
        lists.add(new QueryObject<>("isValid",BusinessEnum.DATA_EFFECTIVE.getCode()));
        //状态为已预约的课程
        lists.add(new QueryObject<>("classStatus",BusinessEnum.COURSE_STATUS_YYY.getCode()));
        //在6个小时内的课程
        lists.add(new QueryObject<>("classTime",sixHours, QueryEnum.LESS_THEN));
        List<TStudentSchoolTimetable> timetables = tStudentSchoolTimetableService.findByParams(lists, QueryEnum.QUERY_AND);
        sendMessage(timetables,BusinessEnum.GO_TO_CLASS_HAD_SEND_MESSAGE_6_HOUR.getCode());
        //设置 查询30分钟内就要上课的课程,只查已经发过一次消息的，也就是6个小时已经发送过一次的 条件
        lists.forEach(item->{
            //设置只查发送过一次6小时内消息的记录
            if(StringUtils.equals(item.getKey(),"isSendClass")){
                item.setValue(BusinessEnum.GO_TO_CLASS_HAD_SEND_MESSAGE_HALF_HOUR.getCode());
            }
            //设置只查30分钟内就要上课的课程
            if(StringUtils.equals(item.getKey(),"classTime")){
                item.setValue(halfHour);
            }
        });
        List<TStudentSchoolTimetable> halfTimeTable = tStudentSchoolTimetableService.findByParams(lists, QueryEnum.QUERY_AND);
        sendMessage(halfTimeTable,BusinessEnum.GO_TO_CLASS_HAD_SEND_MESSAGE_HALF_HOUR.getCode());
    }

    /**
     * 发送上课提醒消息
     * @param timetables 要发送的上课提醒列表
     * @param isSendClass 2:6小时内提醒；3：30分钟内提醒
     */
    private void sendMessage( List<TStudentSchoolTimetable> timetables,String isSendClass){
        //消息列表
        List<TSysMessage> messageList = new ArrayList<>();
        timetables.forEach(item->{
            TWeixinUser u = new TWeixinUser();
            u.setUserId(item.getStudentUserId());
            List<TWeixinUser> userL = tWeixinUserService.findByParams(u);
            if(userL.size()>0){
                if(StringUtils.isNotEmpty(userL.get(0).getOpenid())){
                    //发送即将上课消息提醒
                    weixinXcxService.sendMessage(weixinXcxProperties.getAskLeave(),userL.get(0).getOpenid(),item.getStudentUserName()+"，您有课即将开始，请注意时间！",
                            item.getClassTime(),item.getTeacherUserName()+"老师的课");
                    item.setIsSendClass(isSendClass);
                    TSysMessage message = new TSysMessage();
                    message.setIsRead(BusinessEnum.MESSAGE_NOT_READ.getCode());
                    message.setIsSend(BusinessEnum.MESSAGE_HAD_SEND.getCode());
                    message.setUserId(item.getStudentUserId());
                    message.setUserName(item.getStudentUserName());
                    message.setOpenid(userL.get(0).getOpenid());
                    message.setSendTime(DateJdk8Utils.getNowTime(GlobalConstants.YYYY_MM_DD_HH_MM_SS));
                    message.setSysContent(item.getStudentUserName()+"，您有课即将开始，请注意时间！上课时间："+item.getClassTime()+","+item.getTeacherUserName()+"的课");
                    messageList.add(message);
                }

            }
        });
        //更新课程表发送消息状态
        tStudentSchoolTimetableService.saveAll(timetables);
        //消息入库
        tSysMessageService.saveAll(messageList);
    }

    /**
     * 更新待安排课程表
     */
    private void updateTobeArrange(){
        TStudentCourseTobeArranged tobeArranged = new TStudentCourseTobeArranged();
        tobeArranged.setCourseStatus(BusinessEnum.COURSE_TOBE_ARRANGED_DAP.getCode());
        //只查有效的数据
        tobeArranged.setIsValid(BusinessEnum.DATA_EFFECTIVE.getCode());
        //只查待安排的课程表记录
        List<TStudentCourseTobeArranged> byParams = tStudentCourseTobeArrangedService.findByParams(tobeArranged);
        byParams.forEach(item->{
            long toNowMinutes = DateJdk8Utils.getToNowMinutes(item.getClassTime(), GlobalTimeConstants.YYYY_MM_DD_HH_MM);
            //如果已经到了上课事件，则将待安排课程表记录移到课程表，然后更新待安排课程表该记录为已安排
            if(toNowMinutes<0){
                TStudentSchoolTimetable timetable = new TStudentSchoolTimetable();
                //课程状态为已预约
                timetable.setClassStatus(BusinessEnum.COURSE_STATUS_YYY.getCode());
                timetable.setTeacherUserName(item.getTeacherUserName());
                timetable.setTeacherUserId(item.getTeacherUserId());
                timetable.setStudentUserName(item.getStudentUserName());
                timetable.setStudentUserId(item.getStudentUserId());
                //上课时间
                timetable.setClassTime(item.getClassTime());
                //往课程表新增一条记录
                timetable = tStudentSchoolTimetableService.saveAndFlush(timetable);
                log.info("往课程表新增一条记录：{}",timetable);
                //将待安排课程表里面的该条记录的状态更改为已安排
                item.setCourseStatus(BusinessEnum.COURSE_TOBE_ARRANGED_YAP.getCode());
                item.setUpdateTime(new Date());
                //更新待安排课程表
                item = tStudentCourseTobeArrangedService.saveAndFlush(item);
                log.info("更新待安排课程表：{}",item);
            }
        });

    }

    /**
     * 更新课程表
     */
    private void updateTimeTable(){
        TStudentSchoolTimetable timetable = new TStudentSchoolTimetable();
        timetable.setIsProduceOrder(BusinessEnum.COURSE_ORDER_HAD_PRODUCE.getCode());
        //只查有效的数据
        timetable.setIsValid(BusinessEnum.DATA_EFFECTIVE.getCode());
        //只查还未生成课程工单的
        List<TStudentSchoolTimetable> byParams = tStudentSchoolTimetableService.findByParams(timetable);
        //存已上完课的课程
        List<TStudentSchoolTimetable> timeOverClassList = new ArrayList<>();
        //存将要生产的已上完课的课程工单信息
        List<TClassOrder> classOrderList = new ArrayList<>();
        byParams.forEach(item->{
            long toNowMinutes = DateJdk8Utils.getToNowMinutes(item.getClassTime(), GlobalTimeConstants.YYYY_MM_DD_HH_MM);
            //课已经上完后，更新课程表状态
            if(toNowMinutes+ GlobalConstants.classTimeLenght<0){
                TClassOrder order = new TClassOrder();
                order.setIsEvaluate(BusinessEnum.CLASS_ORDER_EVALUATE_NO.getCode());
               //如果课程表的状态为已预约状态，则更新为已上课状态，否则维持原状
                order.setClassStatus(item.getClassStatus().equals(BusinessEnum.COURSE_STATUS_YYY.getCode())?BusinessEnum.COURSE_STATUS_YSK.getCode():item.getClassStatus());
                order.setClassTime(item.getClassTime());
                order.setStudentUserId(item.getStudentUserId());
                order.setStudentUserName(item.getStudentUserName());
                order.setTeacherUserId(item.getTeacherUserId());
                order.setTeacherUserName(item.getTeacherUserName());
                //课程表id
                order.setTimeTableId(item.getId());
                classOrderList.add(order);
                //新增课程工单
                log.info("往课程工单表新增一条记录：{}",order);
                //如果是已预约状态，则更新课程表状态为已上课状态
                if(StringUtils.equals(item.getClassStatus(),BusinessEnum.COURSE_STATUS_YYY.getCode())){
                   item.setClassStatus(BusinessEnum.COURSE_STATUS_YSK.getCode());
                }
                //更新状态为已经生成课程工单的状态
                item.setIsProduceOrder(BusinessEnum.COURSE_ORDER_HAD_NOT_PRODUCE.getCode());
                timeOverClassList.add(item);
                log.info("更新课程表将该条信息更新为已生成课程工单：{}",item);
            }
        });
        //批量更新课程表状态为已上课
        List<TStudentSchoolTimetable> timetableList = tStudentSchoolTimetableService.saveAll(timeOverClassList);
        //批量生成课程工单
        List<TClassOrder> orderList = tClassOrderService.saveAll(classOrderList);
        log.info("要更新的新课程表课程：将要更新数{}，最终更新数{};要生成课程工单：将要生成数{}，最终生成数{}",
                timeOverClassList.size(),timetableList.size(),classOrderList.size(),orderList.size());
    }




}
