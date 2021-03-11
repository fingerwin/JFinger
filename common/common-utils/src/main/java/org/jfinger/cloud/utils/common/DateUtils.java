package org.jfinger.cloud.utils.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @program: UpFramework
 * @description: 日期工具类
 * @author: lvweifeng
 * @create: 2020-02-14 10:26
 **/
public class DateUtils {
    public static final String DATE_PATTERN_NO_SPLIT = "yyyyMMdd";

    public static final String DATE_PATTERN_DEFAULT = "yyyy-MM-dd";

    public static final String DATETIME_PATTERN_NO_SPLIT = "yyyyMMddHHmmss";

    public static final String DATETIME_PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 根据日期和格式来格式化日期
     *
     * @param date   日期
     * @param format 格式
     * @return
     */
    public static String format(final Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 根据日期和格式来解析日期
     *
     * @param date   日期
     * @param format 格式
     * @return
     * @throws ParseException
     */
    public static Date parse(String date, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(date);
    }

    /**
     * 以yyyy-MM-dd返回日期
     *
     * @param date 日期
     * @return 格式化后的日期字符串
     */
    public static String formatDate(final Date date) {
        return format(date, DATE_PATTERN_DEFAULT);
    }

    /**
     * 以yyyyMMdd返回日期
     *
     * @param date 日期
     * @return 格式化后的日期字符串
     */
    public static String formatNoSplitDate(final Date date) {
        return format(date, DATE_PATTERN_NO_SPLIT);
    }

    /**
     * 以yyyy-MM-dd HH:mm:ss返回时间
     *
     * @param date 日期
     * @return 格式化后的时间字符串
     */
    public static String formatDateTime(final Date date) {
        return format(date, DATETIME_PATTERN_DEFAULT);
    }

    /**
     * 以yyyyMMddHHmmss返回时间
     *
     * @param date 日期
     * @return 格式化后的时间字符串
     */
    public static String formatNoSplitDateTime(final Date date) {
        return format(date, DATETIME_PATTERN_NO_SPLIT);
    }

    /**
     * 以yyyy-MM-dd解析日期
     *
     * @param date 日期
     * @return 解析后的时间
     */
    public static Date parseDate(final String date) throws ParseException {
        return parse(date, DATE_PATTERN_DEFAULT);
    }

    /**
     * 以yyyyMMdd解析日期
     *
     * @param date 日期
     * @return 解析后的时间
     */
    public static Date parseNoSplitDate(final String date) throws ParseException {
        return parse(date, DATE_PATTERN_NO_SPLIT);
    }

    /**
     * 以yyyy-MM-dd HH:mm:ss解析时间
     *
     * @param date 日期
     * @return 解析后的时间
     */
    public static Date parseDateTime(final String date) throws ParseException {
        return parse(date, DATETIME_PATTERN_DEFAULT);
    }

    /**
     * 以yyyyMMddHHmmss解析时间
     *
     * @param date 日期
     * @return 解析后的时间
     */
    public static Date parseNoSplitDateTime(final String date) throws ParseException {
        return parse(date, DATETIME_PATTERN_NO_SPLIT);
    }

    /**
     * 返回今天凌晨0时的时间
     *
     * @return
     */
    public static Date todayStart() {
        return dayStart(new Date());
    }

    /**
     * 返回今天最晚的时间，即23:59:59.999
     *
     * @return
     */
    public static Date todayEnd() {
        return dayEnd(new Date());
    }

    /**
     * 返回指定时间的凌晨0时时间
     *
     * @param date
     * @return
     */
    public static Date dayStart(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 返回指定时间的最晚时间
     *
     * @param date
     * @return
     */
    public static Date dayEnd(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 返回当前周一的开始时间
     *
     * @return
     */
    public static Date weekStart() {
        return weekStart(new Date());
    }

    /**
     * 返回当天周末的最后时间
     *
     * @return
     */
    public static Date weekEnd() {
        return weekEnd(new Date());
    }

    /**
     * 返回指定日期周一的开始时间
     *
     * @param date
     * @return
     */
    public static Date weekStart(Date date) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 0)
            dayOfWeek = 7;
        cd.add(Calendar.DAY_OF_MONTH, 1 - dayOfWeek);
        return dayStart(cd.getTime());
    }

    /**
     * 返回指定日期周末的最后时间
     *
     * @param date
     * @return
     */
    public static Date weekEnd(Date date) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 0)
            dayOfWeek = 7;
        cd.add(Calendar.DAY_OF_MONTH, 7 - dayOfWeek);
        return dayEnd(cd.getTime());
    }

    /**
     * 获取当前月的开始时间
     *
     * @return
     */
    public static Date monthStart() {
        return monthStart(new Date());
    }

    /**
     * 获取当前月末结束时间
     *
     * @return
     */
    public static Date monthEnd() {
        return monthEnd(new Date());
    }

    /**
     * 获取指定时间月的开始时间
     *
     * @param date
     * @return
     */
    public static Date monthStart(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return dayStart(calendar.getTime());
    }

    /**
     * 获取指定日期月末结束时间
     *
     * @param date
     * @return
     */
    public static Date monthEnd(final Date date) {
        Date monthStart = monthStart(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(monthStart);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return dayEnd(calendar.getTime());
    }

    /**
     * 增加月
     *
     * @param date   原日期
     * @param months 月数
     * @return
     */
    public static Date addMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    /**
     * 增加天
     *
     * @param date 原日期
     * @param days 天数
     * @return
     */
    public static Date addDays(final Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return new Date(cal.getTime().getTime());
    }

    /**
     * 增加小时
     *
     * @param date  原日期
     * @param hours 小时数
     * @return
     */
    public static Date addHours(final Date date, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        return new Date(cal.getTime().getTime());
    }

    /**
     * 增加分钟
     *
     * @param date    原日期
     * @param minutes 分钟数
     * @return
     */
    public static Date addMinutes(final Date date, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return new Date(cal.getTime().getTime());
    }

    /**
     * 增加秒
     *
     * @param date    原日期
     * @param seconds 秒数
     * @return
     */
    public static Date addSeconds(final Date date, int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        return new Date(cal.getTime().getTime());
    }

    /**
     * 比较两个时间是否同一个月
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameMonth(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameMonth(cal1, cal2);
    }

    /**
     * 比较两个时间是否同一个月
     *
     * @param cal1
     * @param cal2
     * @return
     */
    public static boolean isSameMonth(Calendar cal1, Calendar cal2) {
        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) && (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH));
    }

    /**
     * 计算2个日期之间相差的天数
     *
     * @param cal1
     * @param cal2
     * @return
     */
    public static int daysBetween(Calendar cal1, Calendar cal2) {
        cal1.clear(Calendar.MILLISECOND);
        cal1.clear(Calendar.SECOND);
        cal1.clear(Calendar.MINUTE);
        cal1.clear(Calendar.HOUR_OF_DAY);

        cal2.clear(Calendar.MILLISECOND);
        cal2.clear(Calendar.SECOND);
        cal2.clear(Calendar.MINUTE);
        cal2.clear(Calendar.HOUR_OF_DAY);

        long elapsed = cal2.getTime().getTime() - cal1.getTime().getTime();
        return Long.valueOf(elapsed / (1000 * 3600 * 24)).intValue();
    }

    /**
     * 计算2个日期之间相差的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int daysBetween(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return daysBetween(cal1, cal2);
    }

    /**
     * 计算2个日期之间相差的小时数
     *
     * @param cal1
     * @param cal2
     * @return
     */
    public static int hoursBetween(Calendar cal1, Calendar cal2) {
        cal1.clear(Calendar.MILLISECOND);
        cal1.clear(Calendar.SECOND);
        cal1.clear(Calendar.MINUTE);

        cal2.clear(Calendar.MILLISECOND);
        cal2.clear(Calendar.SECOND);
        cal2.clear(Calendar.MINUTE);

        long elapsed = cal2.getTime().getTime() - cal1.getTime().getTime();
        return Long.valueOf(elapsed / (1000 * 3600)).intValue();
    }

    /**
     * 计算2个日期之间相差的小时数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int hoursBetween(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return hoursBetween(cal1, cal2);
    }

    /**
     * 计算2个日期之间相差的小时数
     *
     * @param cal1
     * @param cal2
     * @return
     */
    public static int minutesBetween(Calendar cal1, Calendar cal2) {
        cal1.clear(Calendar.MILLISECOND);
        cal1.clear(Calendar.SECOND);

        cal2.clear(Calendar.MILLISECOND);
        cal2.clear(Calendar.SECOND);

        long elapsed = cal2.getTime().getTime() - cal1.getTime().getTime();
        return Long.valueOf(elapsed / (1000 * 60)).intValue();
    }

    /**
     * 计算2个日期之间相差的小时数
     *
     * @param date1
     * @param date2
     * @returnFormatUtils
     */
    public static int minutesBetween(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return minutesBetween(cal1, cal2);
    }
}
