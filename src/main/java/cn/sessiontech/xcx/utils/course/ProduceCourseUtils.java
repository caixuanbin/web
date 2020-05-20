package cn.sessiontech.xcx.utils.course;

import cn.sessiontech.xcx.constant.GlobalConstants;
import cn.sessiontech.xcx.constant.GlobalTimeConstants;
import cn.sessiontech.xcx.entity.TStudentCourseRule;
import cn.sessiontech.xcx.utils.DateJdk8Utils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xbcai
 * @classname ProduceCourseUtils
 * @description 生成课程表工具类
 * @date 2019/10/6 16:28
 */
public class ProduceCourseUtils {
    /**
     * 根据学生的上课规则时间表，生成下一阶段的上课时间
     * @param lastTime 学生课程表的最后一节课的上课时间 yyyy-MM-dd HH:mm
     * @param size 要新增几节课
     * @param courseRule 学生的上课规则
     */
    @SuppressWarnings("all")
    public static List<String> getClassTimeList(String lastTime, int size, TStudentCourseRule courseRule){
        List<String> classTimeList = new ArrayList<>();
        //学生的上课规则表
        Map<String,String[]> map = setStudentRuleToMap(courseRule);
        //获取该学生最新课程表最后的上课时间的下一天是礼拜几
        String nextDayOfWeek = DateJdk8Utils.getNextDayOfWeek(lastTime, GlobalTimeConstants.YYYY_MM_DD_HH_MM);
        //获取该学生最新课程表最后的上课时间的下一天的日期yyyy-MM-dd
        String nextDay = DateJdk8Utils.plusDay(lastTime,GlobalTimeConstants.YYYY_MM_DD_HH_MM);
        for(int i=0;i<size;i++){
            //某一天的上课时间表
            String[] dayClassTime = map.get(nextDayOfWeek);
            //如果下一天该规则表里面没有设置上课时间，则直接跳过那天进入那天的下一天
            if(dayClassTime==null||dayClassTime.length==0){
                //回退
                i=i-1;
                //获取下一天是礼拜几
                nextDayOfWeek =  DateJdk8Utils.getNextDayOfWeek(nextDay, GlobalTimeConstants.YYYY_MM_DD);
                //获取下一天的时间yyyy-MM-dd
                nextDay = DateJdk8Utils.plusDay(nextDay,GlobalTimeConstants.YYYY_MM_DD);
                continue;
            }
            //如果这天设置的上课时间规则时间段还不够分配请假的时间段
            if(dayClassTime.length<(size-i)&&dayClassTime.length>1){
                for(int j=0;j<dayClassTime.length;j++){
                    classTimeList.add(nextDay+" "+dayClassTime[j]);
                }
                //加上时间段的个数
                i= i+dayClassTime.length-1;
                //获取下一天是礼拜几
                nextDayOfWeek =  DateJdk8Utils.getNextDayOfWeek(nextDay, GlobalTimeConstants.YYYY_MM_DD);
                //获取下一天的时间yyyy-MM-dd
                nextDay = DateJdk8Utils.plusDay(nextDay,GlobalTimeConstants.YYYY_MM_DD);
            }else if(dayClassTime.length>=(size-i)&&dayClassTime.length>1){// 如果剩下的请假时间数少于这一天规则的时间段
                for(int j=0;j<size-i;j++){
                    classTimeList.add(nextDay+" "+dayClassTime[j]);
                }
                //加上时间段的个数
                i= i+dayClassTime.length-1;
            }else{//这一天规则表里面只有一节课时间段
                classTimeList.add(nextDay+" "+dayClassTime[0]);
                //获取下一天是礼拜几
                nextDayOfWeek =  DateJdk8Utils.getNextDayOfWeek(nextDay, GlobalTimeConstants.YYYY_MM_DD);
                //获取下一天的时间yyyy-MM-dd
                nextDay = DateJdk8Utils.plusDay(nextDay,GlobalTimeConstants.YYYY_MM_DD);
            }
        }
        return classTimeList;

    }


    /**
     * 将学生规则表转换成map集合，key:礼拜几，value 是上课时间的数组
     * @param rule 学生上课规则
     */
    private static Map<String,String[]> setStudentRuleToMap(TStudentCourseRule rule){
        Map<String,String[]> map = new HashMap<>(8);
        if(StringUtils.isNotEmpty(rule.getRuleMonday())){
            map.put(GlobalConstants.RULE_MONDAY,rule.getRuleMonday().split(","));
        }
        if(StringUtils.isNotEmpty(rule.getRuleTuesday())){
            map.put(GlobalConstants.RULE_TUESDAY,rule.getRuleTuesday().split(","));
        }
        if(StringUtils.isNotEmpty(rule.getRuleWednesday())){
            map.put(GlobalConstants.RULE_WEDNESDAY,rule.getRuleWednesday().split(","));
        }
        if(StringUtils.isNotEmpty(rule.getRuleThursday())){
            map.put(GlobalConstants.RULE_THURSDAY,rule.getRuleThursday().split(","));
        }
        if(StringUtils.isNotEmpty(rule.getRuleFriday())){
            map.put(GlobalConstants.RULE_FRIDAY,rule.getRuleFriday().split(","));
        }
        if(StringUtils.isNotEmpty(rule.getRuleSaturday())){
            map.put(GlobalConstants.RULE_SATURDAY,rule.getRuleSaturday().split(","));
        }
        if(StringUtils.isNotEmpty(rule.getRuleSunday())){
            map.put(GlobalConstants.RULE_SUNDAY,rule.getRuleSunday().split(","));
        }
        return map;
    }
}
