package cn.sessiontech.xcx.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * @author xbcai
 * @classname DateJdk8Utils
 * @description jdk8 日期类型
 * @date 2019/7/30 17:36
 */
public class DateJdk8Utils {
    /**
     * @param dayPattern 要格式的时间格式
     * @return 返回格式化后的时间
     */
    public static String getNowTime(String dayPattern){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dayPattern);
        return formatter.format(localDateTime);
    }

    /**
     * 判断给定日期是否在此刻日期之前
     * @param time 比较的日期
     * @param dayPattern 要格式的时间格式
     * @return true 在此刻之前，false 在此刻之后
     */
    public static boolean isBeforeNow(String time,String dayPattern){
        return LocalDateTime.parse(time,DateTimeFormatter.ofPattern(dayPattern)).isBefore(LocalDateTime.now());
    }

    /**
     * 获取到给定日期还有多少分钟的时间，正数代表此刻在给定日期之前，负数代表此刻在给定日期之后
     * @param time 给定的日期
     * @param dayPattern 时间格式
     */
    public static long getToNowMinutes(String time,String dayPattern){
        LocalDateTime parse = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(dayPattern));
        return ChronoUnit.MINUTES.between(LocalDateTime.now(),parse);
    }

    /**
     * 给定日期，返回礼拜几
     * @param time 给定的日期
     * @param dayPattern 时间格式
     */
    public static String getDayOfWeek(String time,String dayPattern){
        DayOfWeek dayOfWeek = LocalDateTime.parse(time,DateTimeFormatter.ofPattern(dayPattern)).getDayOfWeek();
        return dayOfWeek.toString().toLowerCase();
    }

    /**
     * 给定日期，返回给定日期的下一天是礼拜几
     * @param time 给定日期
     * @param dayPattern 时间格式
     */
    public static String getNextDayOfWeek(String time,String dayPattern){
        DayOfWeek dayOfWeek = LocalDate.parse(time,DateTimeFormatter.ofPattern(dayPattern)).plusDays(1).getDayOfWeek();
        return dayOfWeek.toString().toLowerCase();
    }

    /**
     * 给定日期，返回下一天
     * @param time 给定的日期
     * @param dayPattern 时间格式
     */
    public static String plusDay(String time,String dayPattern){
        LocalDate localDate = LocalDate.parse(time, DateTimeFormatter.ofPattern(dayPattern)).plusDays(1);
        return localDate.toString();
    }

    /**
     * 比较时间是否在给定的时间之前 time 在appoinTime 之前 返回true,否则返回false
     * @param time 时间
     * @param appointTime 指定的时间
     * @param dayPattern 时间格式
     */
    public static boolean isBeforeAppointTime(String time,String appointTime,String dayPattern){
        boolean before = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(dayPattern))
                .isBefore(LocalDateTime.parse(appointTime, DateTimeFormatter.ofPattern(dayPattern)));
        return before;
    }

    /**
     * 获取指定时间的前一天的日期
     * @param time 指定的时间
     * @param dayPattern 时间格式 yyyy-MM-dd
     */
    public static String beforeDay(String time,String dayPattern){
        return LocalDate.parse(time, DateTimeFormatter.ofPattern(dayPattern)).plusDays(-1).toString();
    }

    /**
     * 获取指定单位的，指定数之前的时间,例如：获取距离现在6个小时的时间点时间，格式为yyyy-MM-dd HH:mm
     * @param num 指定数字
     * @param unit 指定单位 例如用ChronoUnit实现类
     * @param dayPattern 指定返回格式
     * @return 返回时间
     */
    public static String minus(long num, TemporalUnit unit,String dayPattern){
        String now = getNowTime(dayPattern);
        LocalDateTime minus = LocalDateTime.parse(now, DateTimeFormatter.ofPattern(dayPattern)).minus(num, unit);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dayPattern);
        return formatter.format(minus);
    }

    public static void main(String[] args) {
        System.out.println(isBeforeNow("2019-10-05 17:00","yyyy-MM-dd HH:mm"));
        LocalDate localDate = LocalDate.parse("2019-10-05", DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(1);
        System.out.println(localDate.toString());
        System.out.println(beforeDay("2019-10-01","yyyy-MM-dd"));
        System.out.println(getNowTime("yyyy-MM-dd HH:mm"));


        System.out.println(minus(6,ChronoUnit.HOURS,"yyyy-MM-dd HH:mm"));
    }




}
