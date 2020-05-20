package cn.sessiontech.xcx.controller;

import cn.sessiontech.xcx.aspect.AnnotationLogs;
import cn.sessiontech.xcx.common.PageResult;
import cn.sessiontech.xcx.common.QueryObject;
import cn.sessiontech.xcx.common.Result;
import cn.sessiontech.xcx.constant.GlobalConstants;
import cn.sessiontech.xcx.controller.base.BaseCtrl;
import cn.sessiontech.xcx.dto.course.*;
import cn.sessiontech.xcx.dto.event.PassOnEventDTO;
import cn.sessiontech.xcx.entity.TClassHourBuyDetail;
import cn.sessiontech.xcx.entity.TClassOrder;
import cn.sessiontech.xcx.entity.TStudentCourseRule;
import cn.sessiontech.xcx.entity.TStudentCourseRuleHistory;
import cn.sessiontech.xcx.entity.sys.TSysUser;
import cn.sessiontech.xcx.enums.BusinessEnum;
import cn.sessiontech.xcx.enums.LoginEnum;
import cn.sessiontech.xcx.enums.RedisEnum;
import cn.sessiontech.xcx.enums.ResultCodeEnum;
import cn.sessiontech.xcx.enums.common.QueryEnum;
import cn.sessiontech.xcx.event.GlobalEvent;
import cn.sessiontech.xcx.service.*;
import cn.sessiontech.xcx.service.sys.TSysUserService;
import cn.sessiontech.xcx.utils.ConverterUtils;
import cn.sessiontech.xcx.utils.JpaUtils;
import cn.sessiontech.xcx.utils.RedisKeyUtils;
import cn.sessiontech.xcx.utils.ThreadUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xbcai
 * @classname TCourseCtrl
 * @description 课程控制类
 * @date 2019/10/4 19:02
 */
@RestController
@RequestMapping("/course")
public class TCourseCtrl extends BaseCtrl {
    @Autowired
    private TSysUserService tSysUserService;
    @Autowired
    private TStudentCourseRuleService tStudentCourseRuleService;
    @Autowired
    private TStudentSchoolTimetableService tStudentSchoolTimetableService;
    @Autowired
    private TClassHourBuyDetailService tClassHourBuyDetailService;
    @Autowired
    private TClassOrderService tClassOrderService;
    @Autowired
    private TStudentCourseTobeArrangedService tStudentCourseTobeArrangedService;
    @Autowired
    private ApplicationContext act;
    @Autowired
    private RedisKeyUtils redisKeyUtils;
    @Autowired
    private TStudentCourseRuleHistoryService tStudentCourseRuleHistoryService;

    /**
     * 获取学生上课规则
     * @param userId 用户id
     */
    @GetMapping("/getCourseRuleByUserId/{userId}")
    public Result getCourseRuleByUserId(@PathVariable("userId")String userId){
        TStudentCourseRule rule = new TStudentCourseRule();
        rule.setUserId(userId);
        return Result.success(tStudentCourseRuleService.findEntityByParams(rule));
    }
    @AnnotationLogs(description = "修改规则")
    @PostMapping("/modifyCourseRule")
    public Result modifyCourseRule(@RequestBody @Valid UserCourseRuleDTO dto,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS,bindingResult.getAllErrors());
        }
        //规则更新及备份
        saveNewAndOldRule(dto);
        //如果是学生，则重新计算他的课程表
        if(StringUtils.equals(LoginEnum.LOGIN_USER_IDENTIFY_STUDENT.getKey(),dto.getModifyType())){
            //对未上课的课程表从生效的时间起会产生影响
            PassOnEventDTO eventDTO = new PassOnEventDTO(GlobalConstants.UPDATE_COURSE_RULE,dto.getUserId());
            //发布事件，将对收到影响的课程重新按新的规则生成课程表
            act.publishEvent(new GlobalEvent<>(eventDTO));
        }
        return Result.success();
    }

    /**
     * 查询学生课程表
     * @param dto 查询条件
     * @param bindingResult 校验信息
     */
    @SuppressWarnings("all")
    @PostMapping("/getStudentTimetable")
    public Result getStudentTimetable(@RequestBody @Valid  TimetableQueryDTO dto, BindingResult bindingResult,HttpServletRequest req){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        List<QueryObject<String,Object>> lists = new ArrayList<>(5);
        //只查有效的数据
        lists.add(new QueryObject<>("isValid",BusinessEnum.DATA_EFFECTIVE.getCode()));
        //根据用户身份信息查询对应范围内的课程表
        PageResult pageResult = filterByUserIdentity(lists, req);
        if(pageResult!=null){return pageResult;}
        //根据入参拼装查询条件
        filterTimeTable(dto,lists);
        PageResult result = tStudentSchoolTimetableService.findByParams(lists,QueryEnum.QUERY_AND,
                new JpaUtils().getPageable(dto.getCurrentPage(),dto.getPageSize(), GlobalConstants.ASC,"classTime"));
        return JpaUtils.convertResult(result);
    }

    /**
     * 查询待安排的课程表
     * @param dto 查询条件
     * @param bindingResult 校验信息
     */
    @SuppressWarnings("all")
    @PostMapping("/getStudentTobeArrangeTimetable")
    public Result getStudentTobeArrangeTimetable(@RequestBody @Valid  TimetableQueryDTO dto, BindingResult bindingResult,HttpServletRequest req){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        List<QueryObject<String,Object>> lists = new ArrayList<>(10);
        //只查有效的数据
        lists.add(new QueryObject<>("isValid",BusinessEnum.DATA_EFFECTIVE.getCode()));
        //根据用户身份信息查询对应范围内的课程表
        PageResult pageResult = filterByUserIdentity(lists, req);
        if(pageResult!=null){return pageResult;}
        //根据入参拼装查询条件
        filterTimeTable(dto,lists);
        PageResult result = tStudentCourseTobeArrangedService.findByParams(lists,QueryEnum.QUERY_AND,
                new JpaUtils().getPageable(dto.getCurrentPage(),dto.getPageSize(), GlobalConstants.DESC,"createTime"));
        return JpaUtils.convertResult(result);
    }

    @AnnotationLogs(description = "购买课时")
    @PostMapping("/buyCourse")
    @SuppressWarnings("all")
    public Result buyCourse(@RequestBody @Valid BuyCourseDTO dto, BindingResult bindingResult, HttpServletRequest req){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }

        Map<String,String> sysUser = (Map<String,String>)req.getSession().getAttribute(RedisEnum.LOGIN_SESSION_USER.getCode());
        String openid = req.getHeader("openid");
        List<String> userIdList = redisKeyUtils.hmget(RedisEnum.HOME_SYS_USER.getCode() + openid, "id");
        TClassHourBuyDetail convert = ConverterUtils.convert(dto, TClassHourBuyDetail.class);
        convert.setClassNum(Integer.valueOf(dto.getClassNum()));
        convert.setOperatorUserId(userIdList.get(0));
        convert.setIsArranged(BusinessEnum.COURSE_BUY_IS_ARRANGED_NO.getCode());
        convert = tClassHourBuyDetailService.saveAndFlush(convert);
        final String id = convert.getId();
        //异步将对应学生，老师的姓名冗余进去
        ThreadUtils.getOhterThreadPool().execute(()->{
            TClassHourBuyDetail buyDetail = tClassHourBuyDetailService.findById(id);
            TSysUser student = tSysUserService.findById(buyDetail.getStudentUserId());
            TSysUser teacher = tSysUserService.findById(buyDetail.getTeacherUserId());
            buyDetail.setStudentUserName(student.getFullName());
            buyDetail.setTeacherUserName(teacher.getFullName());
            tClassHourBuyDetailService.saveAndFlush(buyDetail);
            //如果是扣减课程，则发布事件进行课程的扣减
            if(StringUtils.equals(BusinessEnum.COURSE_BUY_STATUS_CUT.getCode(),buyDetail.getBuyStatus())){
                PassOnEventDTO eventDTO = new PassOnEventDTO(GlobalConstants.DEDUCTION_CLASS,buyDetail.getId());
                //发布事件，扣减课程
                act.publishEvent(new GlobalEvent<>(eventDTO));
            }
        });
        return Result.success();
    }

    /**
     * 获取学生购买课时清单记录
     * @param studentId 学生id
     */
    @GetMapping("/getCourseBuyListByStudentId/{studentId}")
    public Result getCourseBuyListByStudentId(@PathVariable("studentId")String studentId){
        List<QueryObject<String,Object>> lists = new ArrayList<>(5);
        lists.add(new QueryObject<>("studentUserId",studentId));
        List<TClassHourBuyDetail> buyDetails = tClassHourBuyDetailService.findByParamsSort(lists, QueryEnum.QUERY_AND, GlobalConstants.DESC, "createTime");
        return Result.success(buyDetails);
    }

    /**
     * 查询课时列表
     * @param dto 查询条件
     * @param bindingResult 校验信息
     */
    @PostMapping("/getCourseBuyList")
    public Result getCourseBuyList(@RequestBody @Valid BuyCourseQueryDTO dto,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        List<QueryObject<String,Object>> lists = new ArrayList<>(5);
        if(StringUtils.isNotEmpty(dto.getStudentUserName())){
            lists.add(new QueryObject<>("studentUserName",dto.getStudentUserName(),QueryEnum.ALL_LIKE));
        }
        if(StringUtils.isNotEmpty(dto.getTeacherUserName())){
            lists.add(new QueryObject<>("teacherUserName",dto.getTeacherUserName(),QueryEnum.ALL_LIKE));
        }
        if(StringUtils.isNotEmpty(dto.getIsArranged())){
            lists.add(new QueryObject<>("isArranged",dto.getIsArranged()));
        }


        //只查询购买与赠送类型的 1:购买；2：赠送；3：扣减
       lists.add(new QueryObject<>("buyStatus","1,2",QueryEnum.IN));
        PageResult result = tClassHourBuyDetailService.findByParams(lists,QueryEnum.QUERY_AND,
                new JpaUtils().getPageable(dto.getCurrentPage(),dto.getPageSize(), GlobalConstants.DESC,"createTime"));
        return JpaUtils.convertResult(result);
    }

    @AnnotationLogs(description = "设置首次上课时间")
    @PostMapping("/setFirstClassTime")
    public Result setFirstClassTime(@RequestBody @Valid FirstClassTimeDTO dto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        TClassHourBuyDetail buydetail = tClassHourBuyDetailService.findById(dto.getId());
        buydetail.setFirstOnClassTime(dto.getFirstOnClassTime());
        buydetail.setIsArranged(BusinessEnum.COURSE_BUY_IS_ARRANGED_RUNNING.getCode());
        tClassHourBuyDetailService.saveAndFlush(buydetail);
        PassOnEventDTO eventDTO = new PassOnEventDTO(GlobalConstants.SET_FIRST_CLASS_TIME,dto.getId());
        //发布事件，生成课程表
        act.publishEvent(new GlobalEvent<>(eventDTO));
        return Result.success();
    }

    /**
     * 查询课程工单列表
     * @param dto 查询条件
     * @param bindingResult 校验信息
     * @param req 请求信息
     */
    @SuppressWarnings("all")
    @PostMapping("/getClassOrderList")
    public Result getClassOrderList(@RequestBody @Valid ClassOrderQueryDTO dto, BindingResult bindingResult, HttpServletRequest req){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        List<QueryObject<String,Object>> lists = new ArrayList<>(10);
        //根据身份进行过滤
        PageResult pageResult = filterByUserIdentity(lists, req);
        if(pageResult!=null){return pageResult;}
        if(StringUtils.isNotEmpty(dto.getStudentUserName())){
            lists.add(new QueryObject<>("studentUserName",dto.getKeyword(),QueryEnum.ALL_LIKE));
        }
        if(StringUtils.isNotEmpty(dto.getTeacherUserName())){
            lists.add(new QueryObject<>("teacherUserName",dto.getKeyword(),QueryEnum.ALL_LIKE));
        }
        if(StringUtils.isNotEmpty(dto.getBeginTime())){
            lists.add(new QueryObject<>("classTime", dto.getBeginTime(), QueryEnum.GREATER_THAN));
        }
        if(StringUtils.isNotEmpty(dto.getEndTime())){
            lists.add(new QueryObject<>("classTime",dto.getEndTime(),QueryEnum.LESS_THEN));
        }
        PageResult result = tClassOrderService.findByParams(lists,QueryEnum.QUERY_AND,new JpaUtils().getPageable(dto.getCurrentPage(),dto.getPageSize(), GlobalConstants.DESC,"createTime"));
        return JpaUtils.convertResult(result);
    }



    @AnnotationLogs(description = "评价课程")
    @SuppressWarnings("all")
    @PostMapping("/evaluateClassOrder")
    public Result evaluateClassOrder(@RequestBody @Valid  EvaluateClassOrderDTO dto,BindingResult bindingResult,HttpServletRequest req){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        Map<String,String> sysUser  = (Map<String,String>)req.getSession().getAttribute(RedisEnum.LOGIN_SESSION_USER.getCode());
        String openid = req.getHeader("openid");
        List<String> userIdList = redisKeyUtils.hmget(RedisEnum.HOME_SYS_USER.getCode() + openid, "id");
        TClassOrder classOrder = tClassOrderService.findById(dto.getId());
        classOrder.setEvaluateInfo(dto.getEvaluateInfo());
        classOrder.setEvaluateUserId(userIdList.get(0));
        classOrder.setUpdateTime(new Date());
        classOrder.setIsEvaluate(BusinessEnum.CLASS_ORDER_EVALUATE_YES.getCode());
        tClassOrderService.saveAndFlush(classOrder);
        return Result.success();
    }

    /**
     * 根据身份进行过滤，即运营者及系统管理员及老师可以查询所有信息 ，学生及家长只能查自己范围内的信息
     * @param lists
     * @param req
     * @return
     */
    @SuppressWarnings("all")
    private PageResult filterByUserIdentity(List<QueryObject<String,Object>> lists,HttpServletRequest req){
        String openid = req.getHeader("openid");
        List<String> userList = redisKeyUtils.hmget(RedisEnum.HOME_SYS_USER.getCode() + openid, "id","userIdentity");
        String userIdentity = userList.get(1);
        //如果登录的账号角色是学生，只能查看学生的课程工单
        if(StringUtils.equals(userIdentity, LoginEnum.LOGIN_USER_IDENTIFY_STUDENT.getKey())){
            lists.add(new QueryObject<>("studentUserId",userList.get(0)));

        }else if(StringUtils.equals(userIdentity,LoginEnum.LOGIN_USER_IDENTIFY_PARENT.getKey())){// 如果是家长，只查家长的孩子的信息
            TSysUser user = new TSysUser();
            user.setUserParentId(userList.get(0));
            //查询该家长孩子信息
            List<TSysUser> byParams = tSysUserService.findByParams(user);
            //如果没有孩子信息，则直接返回空信息
            if(byParams.size()==0){
                PageResult result = new PageResult();
                result.setCode(200);
                result.setCurrentPage(0);
                result.setFirst(true);
                result.setPageSize(10);
                result.setTotalCount(0);
                result.setTotalPage(0);
                return result;
            }else{
                List<String> ids = new ArrayList<>();
                byParams.forEach(item-> ids.add(item.getId()));
                String idString = String.join(",",ids);
                lists.add(new QueryObject<>("studentUserId",idString,QueryEnum.IN));
            }
        }
        return null;
    }

    /**
     * 根据入参课程表过滤拼装查询条件
     * @param dto 入参
     * @param lists 拼装集合
     */
    private void filterTimeTable(TimetableQueryDTO dto,List<QueryObject<String,Object>> lists){
        if(StringUtils.isNotEmpty(dto.getStudentUserId())){
            lists.add(new QueryObject<>("studentUserId",dto.getStudentUserId()));
        }
        if(StringUtils.isNotEmpty(dto.getStudentUserName())){
            lists.add(new QueryObject<>("studentUserName",dto.getStudentUserName(),QueryEnum.ALL_LIKE));
        }
        if(StringUtils.isNotEmpty(dto.getTeacherUserName())){
            lists.add(new QueryObject<>("teacherUserName",dto.getTeacherUserName(),QueryEnum.ALL_LIKE));
        }
        if(StringUtils.isNotEmpty(dto.getBeginTime())){
            lists.add(new QueryObject<>("classTime", dto.getBeginTime(), QueryEnum.GREATER_THAN));
        }
        if(StringUtils.isNotEmpty(dto.getEndTime())){
            lists.add(new QueryObject<>("classTime",dto.getEndTime(),QueryEnum.LESS_THEN));
        }
    }

    /**
     * 学生/老师 规则
     * 将旧规则入库到历史规则表，然后更新现有规则表
     * @param dto 新入库规则
     */
    private void saveNewAndOldRule(UserCourseRuleDTO dto){
        TStudentCourseRule rule = new TStudentCourseRule();
        rule.setUserId(dto.getUserId());
        //查出旧有的规则信息
        rule = tStudentCourseRuleService.findEntityByParams(rule);
        TStudentCourseRuleHistory historyRule = ConverterUtils.convert(rule, TStudentCourseRuleHistory.class);
        //将旧的规则入库
        tStudentCourseRuleHistoryService.saveAndFlush(historyRule);
        TStudentCourseRule newRule = ConverterUtils.convert(dto, TStudentCourseRule.class);
        newRule.setId(rule.getId());
        newRule.setCreateTime(rule.getCreateTime());
        //将新规则更新入库
        tStudentCourseRuleService.saveAndFlush(newRule);
    }

    public static void main(String[] args) {
        List<String> ids = new ArrayList<>();
        ids.add("1");ids.add("2");ids.add("3");
        System.out.println(String.join(",", ids));
    }

}
