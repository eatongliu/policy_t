package com.gpdata.wanyou.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    /**
     * method 将字符串类型的日期转换为一个timestamp（时间戳记java.sql.Timestamp）
     * <p>
     * 需要转换为timestamp的字符串
     *
     * @return dataTime timestamp
     */
    public static Timestamp strToTimestamp(String date, String pattern) {
        Timestamp dateTime = null;
        java.text.DateFormat df2 = new SimpleDateFormat(pattern);
        try {
            Date date2 = df2.parse(date);
            dateTime = new Timestamp(date2.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    /***
     * 将timeType转换后的浮点型的值
     */
    public static long getTimeToLong(String date, String format) {
        long time = 0L;
        String timeShowFormat = format;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(timeShowFormat);
            ParsePosition pos = new ParsePosition(0);
            time = formatter.parse(date, pos).getTime();
        } catch (Exception ex) {
            // LOGGER.debug("Format " + timeShowFormat + " error : " +
            // ex);
        }
        return time;
    }

    /**
     * 当月第一天0点
     *
     * @return
     */
    public static Long getStartMonth() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.add(Calendar.MONTH, 0);
        todayStart.set(Calendar.DAY_OF_MONTH, 1);// 当月第一天
        todayStart.set(Calendar.HOUR_OF_DAY, 0); // HOUR是 整点时间 HOUR_OF_DAY是当天0点
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
    }

    /**
     * 周起始
     *
     * @return
     */
    public static Long getStartWeek() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.DAY_OF_WEEK, 2);// 当月第一天
        todayStart.set(Calendar.HOUR_OF_DAY, 0); // HOUR是 整点时间 HOUR_OF_DAY是当天0点
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
    }

    /**
     * 当天0点
     *
     * @return
     */
    public static Long getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0); // HOUR是 整点时间 HOUR_OF_DAY是当天0点
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
    }

    /**
     * 当前时间0点
     *
     * @return
     */
    public static Long getStartHourTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR, 0); // HOUR是 整点时间 HOUR_OF_DAY是当天0点
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
    }

    /**
     * 当天结束时间
     *
     * @return
     */
    public static Long getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime().getTime();
    }

    /**
     * 当前时间
     *
     * @return
     */
    public static Long getNowTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.get(Calendar.HOUR_OF_DAY); // HOUR是 整点时间 HOUR_OF_DAY是当天0点
        todayStart.get(Calendar.MINUTE);
        todayStart.get(Calendar.SECOND);
        todayStart.get(Calendar.MILLISECOND);
        return todayStart.getTime().getTime();
    }

    /**
     * 当前时间转timestamp
     */
    public static Timestamp getNowToTimeStamp(String formatr) {
        Date date = new Date();
        String nowTime = new SimpleDateFormat(formatr).format(date);
        return DateUtils.strToTimestamp(nowTime, formatr);
    }

    /**
     * 根据格林时间戳(科学计数)转中文时间
     *
     * @param time    格林时间字符串
     * @param formart 日期格式
     * @return
     */
    public static String LongStrToMart(String time, String formart) {
        // 判断科学计数法时间
        if (time == null || time.length() <= 0 || time == "NULL"
                || time == "null") {
            time = "无固定期限";
        } else if (time.indexOf("E") > 0) {
            BigDecimal bd = new BigDecimal(time);
            time = bd.toPlainString();
            SimpleDateFormat sdf = new SimpleDateFormat(formart);
            time = sdf.format(new Date(Long.parseLong(time)));
        }

        return time;
    }

    /**
     * 判断科学记数法
     *
     * @param numStr
     * @return
     */
    public static String StrToKexue(String numStr) {
        // 判断科学计数法数字
        if (numStr.indexOf("E") > 0) {
            BigDecimal bd = new BigDecimal(numStr);
            numStr = bd.toPlainString();
        }
        return numStr;
    }

    /**
     * @param dt
     * @return
     */
    public static String timeToString(Date dt, String formart) {
        SimpleDateFormat myFmt = new SimpleDateFormat(formart);
        return myFmt.format(dt);
    }

    public static void main(String[] args) {
        LOGGER.debug("{}", DateUtils.strToTimestamp("2011.06.20",
                "yyyy.MM.dd").getTime());
        String s = "null";
        Date date = new Date();
        LOGGER.debug("{}", date);
        LOGGER.debug("{}", DateUtils.LongStrToMart(date.toString(), "yyyy-MM-dd "));
        LOGGER.debug("{}", DateUtils.getStartMonth() / 1000);
        LOGGER.debug("{}", DateUtils.getNowTime() / 1000);
    }
}
