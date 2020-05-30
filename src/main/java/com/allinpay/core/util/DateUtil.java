package com.allinpay.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateUtil {

    /**
     * 获取对账日期，执行日期前一天
     * 日期格式：yyyymmdd
     *
     * @return
     */
    public static String getCheckAccountDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return getSimpleDateFormat(calendar.getTime(), "yyyyMMdd");
    }

    public static String getSettDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return getSimpleDateFormat(calendar.getTime(), "yyyy-MM-dd");
    }

    /**
     * 将指定日期转换为特定格式
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String getSimpleDateFormat(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    /**
     * 判断传入日期是否为当天
     *
     * @return
     */
    public static boolean isNowDay(String time, String pattern) {
        Calendar calendar = Calendar.getInstance();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(time);
            Date before = simpleDateFormat.parse(getSimpleDateFormat(new Date(), "yyyy-MM-dd"));
            calendar.setTime(before);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date after = calendar.getTime();
            if (date.getTime() >= before.getTime() &&
                    date.getTime() < after.getTime()) {
                return true;
            }
        } catch (Exception e) {
            log.error("日期处理异常：{}", e.getMessage());
        }
        return false;
    }

    /**
     * 基于当前时间 查找最近的下一个星期天
     * 如果当天是星期天，则取当天
     *
     * @return
     */
    public static String getNearlySunday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        for (int i = 0; ; i++) {
            if (calendar.get(Calendar.DAY_OF_WEEK) == calendar.getFirstDayOfWeek()) {
                break;
            }
            if (calendar.get(Calendar.DAY_OF_WEEK) > calendar.getFirstDayOfWeek()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        String nextNearlySunday = simpleDateFormat.format(calendar.getTime());
        return nextNearlySunday;
    }

    /**
     * 基于给定时间 查找下一个星期天
     *
     * @param time
     * @return
     */
    public static String getNearlySunday(String time) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(simpleDateFormat.parse(time));
            if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                log.warn("传入的日期不为星期天");
                return "";
            }
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            return simpleDateFormat.format(calendar.getTime());
        } catch (Exception e) {
            log.error("日期格式解析失败");
            return "";
        }
    }

    /**
     * 计算最近第N个星期日的日期
     *
     * @param counts 周数
     */
    public static String getSimpleSunnday(int counts) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpleDateFormat.parse(getNearlySunday()));
        calendar.add(Calendar.DAY_OF_MONTH, (counts - 1) * 7);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(differenceValue("2020-05-31"));

    }

    /**
     * 比较两个时间的大小
     *
     * @param nearlySunday
     * @param rentEndTime
     * @return 0 表示相等 1 表示前者大于后者 -1 表示前者小于后者
     */
    public static int compare(String nearlySunday, String rentEndTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (StringUtils.isBlank(rentEndTime)) {
            return 1;
        }
        Date before = null;
        Date after = null;
        try {
            before = simpleDateFormat.parse(nearlySunday);
            after = simpleDateFormat.parse(rentEndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (before.getTime() == after.getTime()) {
            return 0;
        } else if (before.getTime() > after.getTime()) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * 计算给定时间与当前时间的差值
     *
     * @param time
     * @return
     */
    public static int differenceValue(String time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = null;
        Date specifiedTime = null;
        try {
            currentDate = dateFormat.parse(dateFormat.format(new Date()));
            specifiedTime = dateFormat.parse(time);
        } catch (ParseException e) {
            log.error("传入的日期格式有误");
        }
        return (int) (specifiedTime.getTime() - currentDate.getTime()) / (24 * 60 * 60 * 1000);
    }

    public static long getDiffSeconds(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(dateStr);
            return (System.currentTimeMillis() - date.getTime()) / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
