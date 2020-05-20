package cn.sessiontech.xcx.utils;

import cn.sessiontech.xcx.constant.GlobalConstants;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author xbcai
 * @classname DateUtils
 * @description 日期工具类
 * @date 2019/7/17 8:37
 */
public class DateUtils {
    /**

     * 字符串转化成日期

     * @param strDate 要转换的字符串日期

     * @param pattern 转换的日期格式

     * @return 返回日期

     */

    public static LocalDateTime formatDate(String strDate, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(strDate,dateTimeFormatter);
    }

    /**
     * 字符串日期转Date
     * @param strDate 要转换的字符串日期
     * @param pattern 转换的日期格式
     * @return 返回date
     */
    public static Date formatString(String strDate, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(strDate);
        }catch (Exception e){
            e.printStackTrace();
        }
        return date;
    }

    public static void main(String[] args) {
        Date date = formatString("2019-07-08 12:12:12", GlobalConstants.YYYY_MM_DD_HH_MM_SS);
        System.out.println(date);
    }
}
