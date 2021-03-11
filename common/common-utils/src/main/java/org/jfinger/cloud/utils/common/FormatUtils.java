/**
 * Copyright (C) 2012 finger.
 * This file write by finger in 2013-1-17,mail:luweifeng_2000@126.com
 */
package org.jfinger.cloud.utils.common;

import java.text.DecimalFormat;

/**
 * 格式化类，提供各种格式化方法
 *
 * @author finger
 * @version V1.0
 */
public class FormatUtils {

    /**
     * 将驼峰命名转化成下划线
     *
     * @param para
     * @return
     */
    public static String camelToUnderline(String para) {
        if (para.length() < 3) {
            return para.toLowerCase();
        }
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;//定位
        //从第三个字符开始 避免命名不规范
        for (int i = 2; i < para.length(); i++) {
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, "_");
                temp += 1;
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 格式化浮点数字
     *
     * @param value  源数据
     * @param format 格式
     * @return 格式化后结果
     */
    public static String formatFloat(double value, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(value);
    }

    /**
     * 格式化浮点数字
     *
     * @param value 源数据
     * @return 格式化后结果
     */
    public static String formatFloat(double value) {
        return formatFloat(value, "0.00");
    }

    /**
     * 格式化分钟时间
     *
     * @param minutes 分钟数
     * @return
     */
    public static String formatMinutes(int minutes) {
        /** 天 */
        int day = 0;
        /** 小时 */
        int hour = 0;
        /** 分钟 */
        int minute = 0;
        /** 天字符串 */
        String dayStr = "";
        /** 小时字符串 */
        String hourStr = "";
        /** 分钟字符串 */
        String minuteStr = "";
        if (minutes > 59) {
            hour = minutes / 60;
            minute = minutes % 60;
        }
        if (hour > 23) {
            day = hour / 24;
            hour = hour % 24;
        }
        return day > 0 ? day + "天" + hour + "小时" + minute + "分" : hour > 0 ? hour + "小时" + minute + "分" : minute + "分";
    }

    /**
     * 格式化时间
     *
     * @param second 时间值(秒)
     * @return 格式化后的时间串
     */
    public static String formatTime(int second) {
        /** 小时 */
        int hour = 0;
        /** 分钟 */
        int minute = 0;
        /** 小时字符串 */
        String hourStr = "";
        /** 分钟字符串 */
        String minuteStr = "";
        /** 秒字符串 */
        String secondStr = "";
        /** 格式化后的时间 */
        String formatTime = "";

        if (second > 59) {
            minute = second / 60;
            second = second % 60;
        }

        if (minute > 59) {
            hour = minute / 60;
            minute = minute % 60;
        }

        if (hour < 10) {
            hourStr = "0" + String.valueOf(hour);
        } else {
            hourStr = String.valueOf(hour);
        }

        if (minute < 10) {
            minuteStr = "0" + String.valueOf(minute);
        } else {
            minuteStr = String.valueOf(minute);
        }

        if (second < 10) {
            secondStr = "0" + String.valueOf(second);
        } else {
            secondStr = String.valueOf(second);
        }

        // 时间为达到小时时，以"00:00"格式化，超过小时时以"00:00:00"格式化
        if (hour > 0) {
            formatTime = hourStr + ":" + minuteStr + ":" + secondStr;
        } else {
            formatTime = minuteStr + ":" + secondStr;
        }

        return formatTime;
    }

    /**
     * 格式化一个文件大小的值
     *
     * @param size 大小，单位B
     * @return
     */
    public static String formatFileSize(long size) {
        if (size < 1024) {
            return size + "B";
        } else if (size < 1024 * 1024) {
            return size / 1024 + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            return size / (1024 * 1024) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            return size / (1024 * 1024 * 1024) + "GB";
        } else
            return "无法计算";
    }

    /**
     * 返回一个格式化的距离字符
     *
     * @param distance 距离
     * @return
     */
    public static String formatDistance(float distance) {
        return formatDistance(distance, false);
    }

    /**
     * 返回一个格式化的距离字符
     *
     * @param distance 距离
     * @return
     */
    public static String formatDistance(float distance, boolean cnUnit) {
        if (distance <= 0) {
            return "";
        } else if (distance < 1000) {
            return Float.valueOf(distance).intValue() + (cnUnit ? "米" : "m");
        } else
            return Float.valueOf(distance / 1000).intValue() + (cnUnit ? "千米" : "km");
    }
}
