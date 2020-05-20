package cn.sessiontech.xcx.listener;

import cn.sessiontech.xcx.common.QueryObject;
import cn.sessiontech.xcx.constant.GlobalConstants;
import cn.sessiontech.xcx.constant.GlobalTimeConstants;
import cn.sessiontech.xcx.dto.event.PassOnEventDTO;
import cn.sessiontech.xcx.entity.*;
import cn.sessiontech.xcx.enums.BusinessEnum;
import cn.sessiontech.xcx.enums.common.QueryEnum;
import cn.sessiontech.xcx.event.GlobalEvent;
import cn.sessiontech.xcx.monitor.GlobalApplicationRunner;
import cn.sessiontech.xcx.service.*;
import cn.sessiontech.xcx.utils.DateJdk8Utils;
import cn.sessiontech.xcx.utils.ThreadUtils;
import cn.sessiontech.xcx.utils.course.ProduceCourseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xbcai
 * @classname GlobalListener
 * @description 监听事件
 * @date 2019/8/25 14:35
 */
@Slf4j
@Component
public class GlobalListener implements ApplicationListener<GlobalEvent> {
    @Autowired
    private GlobalApplicationRunner agr;
    @Autowired
    private TStudentSchoolTimetableService tStudentSchoolTimetableService;
    @Autowired
    private TStudentCourseTobeArrangedService tStudentCourseTobeArrangedService;
    @Autowired
    private TStudentCourseRuleService tStudentCourseRuleService;
    @Autowired
    private TClassHourBuyDetailService tClassHourBuyDetailService;
    @Autowired
    private TClassOrderService tClassOrderService;
    @Override
    public void onApplicationEvent(GlobalEvent event) {
        ThreadUtils.getOhterThreadPool().execute(()->{
            PassOnEventDTO dto = (PassOnEventDTO)event.getSource();
            log.info("监听到请求事件：{}",dto);
            if(StringUtils.equals(GlobalConstants.ASK_LEAVE_STUDENT,dto.getEventType())){
                //学生请假-不扣课时
                askLeaveForStudent(dto);
            }else if(StringUtils.equals(GlobalConstants.ASK_LEAVE_TEACHER,dto.getEventType())){
                //todo 老师请假，将要更新待安排课程表，到时候后就更新课程表
            }else if(StringUtils.equals(GlobalConstants.SET_FIRST_CLASS_TIME,dto.getEventType())){
                // 生成课程表--即学生购买课程或赠送课程后，运营人员设置首次上课时间，然后就按照该学生的上课规则生成课程表
                produceCourse(dto);
            }else if(StringUtils.equals(GlobalConstants.DEDUCTION_CLASS,dto.getEventType())){
                //扣减课程
                reduceClass(dto);
            }else if(StringUtils.equals(GlobalConstants.CANCEL_LEAVE_STUDENT,dto.getEventType())){
                //取消请假--学生在还没上课之前取消请假
                cancelStudentLeave(dto);
            }else if(StringUtils.equals(GlobalConstants.SET_STUDENT_KK,dto.getEventType())){
                //设置课程工单表对应状态为旷课
                setKK(dto);
            }else if(StringUtils.equals(GlobalConstants.UPDATE_COURSE_RULE,dto.getEventType())){
                //更新上课的规则后触发，重新排版现有的课程表，只对学生已有课程表时间大于规则的生效时间那部分的课程有影响，也就在那之后的课程需重新按照新的规则进行生成
                updateCourse(dto);
            }
        });
    }

    /**
     * 扣减课程
     * @param dto 扣减课程的监听
     */
    private void reduceClass(PassOnEventDTO dto){
        //存待安排课程的课程表 id
        List<String> todoList = new ArrayList<>();
        TClassHourBuyDetail buyDetail = tClassHourBuyDetailService.findById(dto.getKey());
        List<QueryObject<String,String>> lists = new ArrayList<>(5);
        lists.add(new QueryObject<>("studentUserId",buyDetail.getStudentUserId()));
        lists.add(new QueryObject<>("teacherUserId",buyDetail.getTeacherUserId()));
        lists.add(new QueryObject<>("isValid",BusinessEnum.DATA_EFFECTIVE.getCode()));
        List<TStudentSchoolTimetable> classTime = tStudentSchoolTimetableService.findByParamsSort(lists, QueryEnum.QUERY_AND, GlobalConstants.DESC, "classTime");
        classTime.stream().limit(buyDetail.getClassNum()).forEach(item->{
            item.setIsValid(BusinessEnum.DATA_INVALID.getCode());
            if(StringUtils.equals(item.getClassStatus(),BusinessEnum.COURSE_STATUS_ASK_LEAVE_BKKS.getCode())){
                todoList.add(item.getId());
            }
            //将课程设置为无效，也就是做逻辑删除
            tStudentSchoolTimetableService.saveAndFlush(item);
            log.info("课程扣减操作，成功扣减一条记录，该课程清单为：{}",item);
        });
        //如果存在待安排的课程表，也就是请假-不扣课时，那也要将待安排课程表里面的对应记录设置为无效
        if(todoList.size()>0){
            List<QueryObject<String,String>> queryList = new ArrayList<>();
            queryList.add(new QueryObject<>("schoolTimetableId",String.join(",",todoList),QueryEnum.IN));
            List<TStudentCourseTobeArranged> byParams = tStudentCourseTobeArrangedService.findByParams(queryList, QueryEnum.QUERY_AND);
            byParams.forEach(item->{
                item.setIsValid(BusinessEnum.DATA_INVALID.getCode());
                tStudentCourseTobeArrangedService.saveAndFlush(item);
                log.info("待课程安排表扣减操作，成功扣减一条记录，该待安排课程清单为：{}",item);
            });
        }
        //设置扣减课程清单为已安排
        buyDetail.setIsArranged(BusinessEnum.COURSE_TOBE_ARRANGED_YAP.getCode());
        //更新扣减记录为已安排
        tClassHourBuyDetailService.saveAndFlush(buyDetail);
        log.info("完成课程扣减整个操作，将扣减记录设置为已安排，扣减清单为：{}",buyDetail);

    }
    /**
     * 重新设置规则后，将符合规则的课程重新安排
     * 重新排版现有的课程表，只对学生已有课程表时间大于规则的生效时间那部分的课程有影响，也就在那之后的课程需重新按照新的规则进行生成
     * @param dto 修改课程规则监听
     */
    private void updateCourse(PassOnEventDTO dto){
        TStudentCourseRule rule = new TStudentCourseRule();
        rule.setUserId(dto.getKey());
        //获取新的规则
        TStudentCourseRule newRule = tStudentCourseRuleService.findEntityByParams(rule);
        String takeEffectTime = newRule.getTakeEffectTime();
        List<QueryObject<String,String>> query = new ArrayList<>();
        query.add(new QueryObject<>("studentUserId",dto.getKey()));
        query.add(new QueryObject<>("classTime",takeEffectTime+GlobalConstants.START_TIME_SUFFIX_H_M,QueryEnum.GREATER_THAN));
         // 1:已上课；2：已预约；3：请假-不扣课时；4：请假-扣课时；5：旷课; 6：老师请假；
        query.add(new QueryObject<>("classStatus","2,3,4,6",QueryEnum.IN));
        //查出要重新安排的课程表
        List<TStudentSchoolTimetable> table = tStudentSchoolTimetableService.findByParams(query, QueryEnum.QUERY_AND);
        //如果有符合重新安排的课程，才进行重新安排
        if(table.size()>0){
            String lastTime = DateJdk8Utils.beforeDay(newRule.getTakeEffectTime(),GlobalTimeConstants.YYYY_MM_DD);
            //根据规则获取新生成的课程表时间
            List<String> classTimeList = ProduceCourseUtils.getClassTimeList(lastTime+" 00:00",table.size(), newRule);
            //将需要重新安排的课程表的有效请假课程的id存放进来，然后要更新待安排课程表对应的记录为失效记录
            List<String> validAskLeavel = new ArrayList<>();
            for(int i=0;i<table.size();i++){
                //如果是 有效请假-不扣课时的 则存到list集合供将待安排课程表对应的记录设置为无效的记录
                if(StringUtils.equals(table.get(i).getClassStatus(),BusinessEnum.COURSE_STATUS_ASK_LEAVE_BKKS.getCode())){
                    validAskLeavel.add(table.get(i).getId());
                }
                //修改课程状态为已预约
                table.get(i).setClassStatus(BusinessEnum.COURSE_STATUS_YYY.getCode());
                //重新设置上课时间
                table.get(i).setClassTime(classTimeList.get(i));

                log.info("重新安排一节课程，课程具体信息如下：{}",table.get(i));
            }
            //更新课程表
            tStudentSchoolTimetableService.saveAll(table);
            if(validAskLeavel.size()>0){
                String ids = String.join(",",validAskLeavel);
                List<QueryObject<String,String>> list = new ArrayList<>(2);
                list.add(new QueryObject<>("schoolTimetableId",ids,QueryEnum.IN));
                List<TStudentCourseTobeArranged> tobeArranged = tStudentCourseTobeArrangedService.findByParams(list, QueryEnum.QUERY_AND);
                //将待安排课程表里面的待安排状态的课程设置为无效
                tobeArranged.forEach(item->{
                    item.setIsValid(BusinessEnum.DATA_INVALID.getCode());
                    log.info("将待安排课程表里面的对应的待安排课程设置为无效，具体信息如下：{}",item);
                    tStudentCourseTobeArrangedService.saveAndFlush(item);
                });
            }
        }

    }

    /**
     * 设置课程工单对应状态为旷课
     * @param dto 旷课事件监听
     */
    private void setKK(PassOnEventDTO dto){
        TClassOrder order = new TClassOrder();
        order.setTimeTableId(dto.getKey());
        TClassOrder entityByParams = tClassOrderService.findEntityByParams(order);
        if(entityByParams!=null){
            //设置课程工单状态为旷课
            entityByParams.setClassStatus(BusinessEnum.COURSE_STATUS_KK.getCode());
            entityByParams.setUpdateTime(new Date());
            entityByParams = tClassOrderService.saveAndFlush(entityByParams);
            log.info("更新课程工单该条课程记录为已旷课：{}",entityByParams);
        }
    }

    /**
     * 取消学生请假
     * @param dto 取消请假监听事件
     */
    private void cancelStudentLeave(PassOnEventDTO dto){
        TStudentCourseTobeArranged tobeArranged = new TStudentCourseTobeArranged();
        tobeArranged.setSchoolTimetableId(dto.getKey());
        TStudentCourseTobeArranged entityByParams = tStudentCourseTobeArrangedService.findEntityByParams(tobeArranged);
        //设置课程为已经取消
        entityByParams.setCourseStatus(BusinessEnum.COURSE_TOBE_ARRANGED_YQX.getCode());
        entityByParams.setUpdateTime(new Date());
        entityByParams = tStudentCourseTobeArrangedService.saveAndFlush(entityByParams);
        log.info("取消待安排课程表对应记录为取消请假：{}",entityByParams);
    }

    /**
     * 生成课程表
     * @param dto 课程表监听事件
     */
    private void produceCourse(PassOnEventDTO dto){
        TClassHourBuyDetail buyDetail = tClassHourBuyDetailService.findById(dto.getKey());
        String firstOnClassTime = buyDetail.getFirstOnClassTime();
        String lastTime = DateJdk8Utils.beforeDay(firstOnClassTime,GlobalTimeConstants.YYYY_MM_DD);

        //校验前端设置的首次上课时间是不是大于已有的课程表的最大时间，如果不是，则取课程表最后一节课的时间，如果没有课程，则默认安装前端设置的首次上课时间
        List<QueryObject<String,String>> lists = new ArrayList<>(5);
        lists.add(new QueryObject<>("isValid",BusinessEnum.DATA_EFFECTIVE.getCode()));
        lists.add(new QueryObject<>("studentUserId",buyDetail.getStudentUserId()));
        lists.add(new QueryObject<>("classTime",buyDetail.getFirstOnClassTime()+GlobalConstants.START_TIME_SUFFIX_H_M,QueryEnum.GREATER_THAN));
        List<TStudentSchoolTimetable> classTime = tStudentSchoolTimetableService.findByParamsSort(lists, QueryEnum.QUERY_AND, GlobalConstants.DESC, "classTime");
        //如果课程表有课程比指定的课程生效时间还要大，则取课程表里面最大的课程的时间年月日
        if(classTime.size()>0){
            String firstTime = classTime.get(0).getClassTime();
            lastTime = firstTime.substring(0,10);
        }
        TStudentCourseRule rule = new TStudentCourseRule();
        rule.setUserId(buyDetail.getStudentUserId());
        //获取学生上课规则
        TStudentCourseRule courseRule = tStudentCourseRuleService.findEntityByParams(rule);
        //根据规则获取新生成的课程表时间
        List<String> classTimeList = ProduceCourseUtils.getClassTimeList(lastTime+" 00:00", buyDetail.getClassNum(), courseRule);
        for(int i=0;i<classTimeList.size();i++){
            TStudentSchoolTimetable timetable = new TStudentSchoolTimetable();
            //设置状态为已预约
            timetable.setClassStatus(BusinessEnum.COURSE_STATUS_YYY.getCode());
            //上课时间
            timetable.setClassTime(classTimeList.get(i));
            //学生id
            timetable.setStudentUserId(buyDetail.getStudentUserId());
            //学生姓名
            timetable.setStudentUserName(buyDetail.getStudentUserName());
            //老师id
            timetable.setTeacherUserId(buyDetail.getTeacherUserId());
            //老师姓名
            timetable.setTeacherUserName(buyDetail.getTeacherUserName());
            //消息未发送默认值
            timetable.setIsSendClass(BusinessEnum.MESSAGE_NOT_SEND.getCode());
            //设置该条数据为有效状态
            timetable.setIsValid(BusinessEnum.DATA_EFFECTIVE.getCode());
            //设置该条课程工单还未生成
            timetable.setIsProduceOrder(BusinessEnum.COURSE_ORDER_HAD_PRODUCE.getCode());
            //新增一条课程表信息
            timetable = tStudentSchoolTimetableService.saveAndFlush(timetable);
            log.info("生成一节课程表信息：{}",timetable);
        }
        //设置预约上课为已安排
        buyDetail.setIsArranged(BusinessEnum.COURSE_BUY_IS_ARRANGED_YES.getCode());
        tClassHourBuyDetailService.saveAndFlush(buyDetail);
    }


    /**
     * 请假不扣课时生成待安排课程表
     * @param dto 从课程表传递的请假
     */

    private void askLeaveForStudent(PassOnEventDTO dto){
     String key = dto.getKey();
     TStudentSchoolTimetable timeTable = tStudentSchoolTimetableService.findById(key);
     List<QueryObject<String,Object>> lists = new ArrayList<>(5);
     lists.add(new QueryObject<>("studentUserId",timeTable.getStudentUserId()));
     List<TStudentSchoolTimetable> classTime = tStudentSchoolTimetableService.findByParamsSort(lists, QueryEnum.QUERY_AND, GlobalConstants.DESC, "classTime");
     //获取课程表中最后一节课的时间
     TStudentSchoolTimetable lastTimeTable = classTime.get(0);
     //获取课程表最后的时间表即最后一节课的上课时间
     String lastTime = lastTimeTable.getClassTime();
     TStudentCourseRule rule = new TStudentCourseRule();
     rule.setUserId(timeTable.getStudentUserId());
     //获取学生上课规则
     TStudentCourseRule courseRule = tStudentCourseRuleService.findEntityByParams(rule);
     //1:待安排；2：已安排；3：已取消；
     lists.add(new QueryObject<>("courseStatus","1,2",QueryEnum.QUERY_OR));
     //获取待安排表里面该学生的课程表时间
     List<TStudentCourseTobeArranged> toBeArrangedList = tStudentCourseTobeArrangedService.findByParamsSort(lists, QueryEnum.QUERY_AND, GlobalConstants.DESC, "classTime");
     if(toBeArrangedList.size()>0){
     //获取待安排课程表里面最新的时间
     String toBeClassTime = toBeArrangedList.get(0).getClassTime();
     boolean flag = DateJdk8Utils.isBeforeAppointTime(lastTime, toBeClassTime, GlobalTimeConstants.YYYY_MM_DD_HH_MM);
     //如果课程表的最后一节课在待安排课程表之前，则取待安排课程表中最新的一节课的时间
     if(flag){
     lastTime = toBeClassTime;
     }
     }
     //根据规则获取新生成的课程表时间
     List<String> classTimeList = ProduceCourseUtils.getClassTimeList(lastTime, 1, courseRule);
     for (int i=0;i<classTimeList.size();i++) {
     TStudentCourseTobeArranged arranged = new TStudentCourseTobeArranged();
     //设置上课时间
     arranged.setClassTime(classTimeList.get(0));
     //设置上课状态为待安排
     arranged.setCourseStatus(BusinessEnum.COURSE_TOBE_ARRANGED_DAP.getCode());
     //设置学生id
     arranged.setStudentUserId(timeTable.getStudentUserId());
     //设置请假的课程表对应的id
     arranged.setSchoolTimetableId(key);
     //设置学生姓名
     arranged.setStudentUserName(timeTable.getStudentUserName());
     //设置老师id
     arranged.setTeacherUserId(timeTable.getTeacherUserId());
     //设置老师姓名
     arranged.setTeacherUserName(timeTable.getTeacherUserName());
     //设置该条数据是有效的
     arranged.setIsValid(BusinessEnum.DATA_EFFECTIVE.getCode());
     //保存待安排课程表
     arranged = tStudentCourseTobeArrangedService.saveAndFlush(arranged);
     log.info("生成一节待安排的课程表信息：{}",arranged);
     }



     }
     @SuppressWarnings("all")
    /**
     * private void askLeaveForStudent(PassOnEventDTO dto){
        String[] strs = dto.getKey().split(",");
        TStudentSchoolTimetable timeTable = tStudentSchoolTimetableService.findById(strs[0]);
        List<QueryObject<String,Object>> lists = new ArrayList<>(5);
        lists.add(new QueryObject<>("studentUserId",timeTable.getStudentUserId()));
        List<TStudentSchoolTimetable> classTime = tStudentSchoolTimetableService.findByParamsSort(lists, QueryEnum.QUERY_AND, GlobalConstants.DESC, "classTime");
        //获取课程表中最后一节课的时间
        TStudentSchoolTimetable lastTimeTable = classTime.get(0);
        //获取课程表最后的时间表即最后一节课的上课时间
        String lastTime = lastTimeTable.getClassTime();
        TStudentCourseRule rule = new TStudentCourseRule();
        rule.setUserId(timeTable.getStudentUserId());
        //获取学生上课规则
        TStudentCourseRule courseRule = tStudentCourseRuleService.findEntityByParams(rule);
        //1:待安排；2：已安排；3：已取消；
        lists.add(new QueryObject<>("courseStatus","1,2",QueryEnum.QUERY_OR));
        //获取待安排表里面该学生的课程表时间
        List<TStudentCourseTobeArranged> toBeArrangedList = tStudentCourseTobeArrangedService.findByParamsSort(lists, QueryEnum.QUERY_AND, GlobalConstants.DESC, "classTime");
        if(toBeArrangedList.size()>0){
            //获取待安排课程表里面最新的时间
            String toBeClassTime = toBeArrangedList.get(0).getClassTime();
            boolean flag = DateJdk8Utils.isBeforeAppointTime(lastTime, toBeClassTime, GlobalTimeConstants.YYYY_MM_DD_HH_MM);
            //如果课程表的最后一节课在待安排课程表之前，则取待安排课程表中最新的一节课的时间
            if(flag){
                lastTime = toBeClassTime;
            }
        }
        //根据规则获取新生成的课程表时间
        List<String> classTimeList = ProduceCourseUtils.getClassTimeList(lastTime, strs.length, courseRule);
        for (int i=0;i<classTimeList.size();i++) {
            TStudentCourseTobeArranged arranged = new TStudentCourseTobeArranged();
            //设置上课时间
            arranged.setClassTime(classTimeList.get(0));
            //设置上课状态为待安排
            arranged.setCourseStatus(BusinessEnum.COURSE_TOBE_ARRANGED_DAP.getCode());
            //设置学生id
            arranged.setStudentUserId(timeTable.getStudentUserId());
            //设置请假的课程表对应的id
            arranged.setSchoolTimetableId(strs[i]);
            //设置学生姓名
            arranged.setStudentUserName(timeTable.getStudentUserName());
            //设置老师id
            arranged.setTeacherUserId(timeTable.getTeacherUserId());
            //设置老师姓名
            arranged.setTeacherUserName(timeTable.getTeacherUserName());
            //保存待安排课程表
            arranged = tStudentCourseTobeArrangedService.saveAndFlush(arranged);
            log.info("生成一节待安排的课程表信息：{}",arranged);
        }



    }
     **/

    public static void main(String[] args) {
        String s ="2019-10-01 10:00";
        String s2 = "2019-10-01 10:01";
        String lastTime = DateJdk8Utils.beforeDay("2019-10-27",GlobalTimeConstants.YYYY_MM_DD);
         System.out.println(lastTime);
         System.out.println(s.substring(0,10));
    }







}
