package com.hjq.demo.util;

import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    /**
     * 获取当前时间
     *
     * @return 毫秒时间戳
     */
    public static String getCurTimeMills() {
        return System.currentTimeMillis()+"";
    }

    /**
     * 获取当前时间的毫秒数
     */
    public static long getTimeLong() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当时时间的年，月，日，时分秒
     * 这里获得的时int类型的数据，要输入对应的格式
     * 比如要获得现在时间的天数值，
     * 使用getTime（“MM”）,如果现在是2016年5月20日，取得的数字是5；
     */
    public static int getTimeInt(String filter) {
        //
        SimpleDateFormat format = new SimpleDateFormat(filter);
        String time = format.format(new Date());
        return Integer.parseInt(time);
    }

    /**
     * 获取指定时间的年，月，日，时分秒
     * 这里获得的时int类型的数据，要输入完整时间的字符串和对应的格式
     * 比如要获得时间2016-12-12 14：12：10的小时的数值，
     * 使用getTime（“2016-12-12 14：12：10”，“HH”）；得到14
     */
    public static int getTimeInt(String StringTime, String filter) {
        //
        SimpleDateFormat format = new SimpleDateFormat(filter);
        String time = format.format(new Date(getTimeLong("yyyy-MM-dd HH:mm:ss", StringTime)));
        return Integer.parseInt(time);
    }


    /**
     * 获取当前时间的完整显示字符串
     */
    public static final String getTimeString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(getTimeLong()));
    }

    /**
     * 获得某个时间的完整格式的字符串
     * 输入的是某个时间的毫秒数，
     * 有些时候文件保存的时间是毫秒数，这时就需要转换来查看时间了！
     */
    public static final String getTimeString(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));
    }

    /**
     * 获得自定义格式的时间字符串
     * 输入的是某个时间的毫秒数，显示的可以是时间字符串的某一部分
     * 比如想要获得某一个时间的月和日，getTimeString(1111111111111,"MM-dd");
     */
    public static final String getTimeString(long time, String filter) {
        SimpleDateFormat format = new SimpleDateFormat(filter);
        return format.format(new Date(time));
    }

    /**
     * 获得自定义格式的当前的时间的字符串
     * 比如当前时间2016年8月8日12时8分21秒，getTimeString("yyyy-MM-dd"),可以得到 2016-8-12
     */
    public static final String getTimeString(String filter) {
        SimpleDateFormat format = new SimpleDateFormat(filter);
        return format.format(new Date(getTimeLong()));
    }

    /**
     * 获取某个时间的毫秒数，
     * 一般作用于时间先后的对比
     * 第一个参数是时间的格式，第二个参数是时间的具体值
     * 比如需要知道2016-6-20的毫秒数（小时和分钟默认为零），
     * getTimeLong("yyyy-MM-dd","2016-6-20")
     * 有时只有月日也是可以的，比如  getTimeLong("MM-dd","6-20") ，一般这个用来比较时间先后
     * 记住获得的毫秒数越大，时间就越近你
     */
    public static Long getTimeLong(String filter, String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(filter);
            Date dateTime = format.parse(date);
            return dateTime.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0L;
    }


    /**
     * 获得某一个时间字符串中的局部字符串
     * 比如：String data= "2016-5-20 12：12：10"，要获得后面的时间：5-20或 12：10
     * 使用：getTimeLocalString("yyyy-MM-dd HH:mm:ss",data,"MM-dd")   ，可以获得5-20
     * 如果是data="2016-5-20"，要获得后面的5-20，
     * 也是一样的用法getTimeLocalString("yyyy-MM-dd ",data,"MM-dd")！
     */
    public static String getTimeLocalString(String filter, String data, String filterInside) {
        Long timeLong = getTimeLong(filter, data);
        return getTimeString(timeLong, filterInside);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowTime() {
        String timeString = null;
        Time time = new Time();
        time.setToNow();
        String year = thanTen(time.year);
        String month = thanTen(time.month + 1);
        String monthDay = thanTen(time.monthDay);
        String hour = thanTen(time.hour);
        String minute = thanTen(time.minute);

        timeString = year + "-" + month + "-" + monthDay + " " + hour + ":" + minute;
        return timeString;
    }

    /**
     * 十一下加零
     *
     * @param str
     * @return
     */
    private static String thanTen(int str) {

        String string = null;

        if (str < 10) {
            string = "0" + str;
        } else {

            string = "" + str;

        }
        return string;
    }

    /**
     * 计算时间差
     *
     * @param starTime 开始时间
     * @param endTime  结束时间
     *                 返回类型 ==1----天，时，分。 ==2----时
     * @return 返回时间差
     */
    public static String getTimeDifference(String starTime, String endTime) {
        String timeString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endTime);

            long diff = parse1.getTime() - parse.getTime();

            long day = diff / (24 * 60 * 60 * 1000);
            long hour = (diff / (60 * 60 * 1000) - day * 24);
            long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            long ms = (diff - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                    - min * 60 * 1000 - s * 1000);
            // System.out.println(day + "天" + hour + "小时" + min + "分" + s +"秒");
            long hour1 = diff / (60 * 60 * 1000);
            String hourString = hour1 + "";
            long min1 = ((diff / (60 * 1000)) - hour1 * 60);
            timeString = hour1 + "小时" + min1 + "分";//String.valueOf(day);
            System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeString;

    }

    /*
     * 将时间戳转换为时间
     *
     * s就是时间戳
     */

    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //如果它本来就是long类型的,则不用写这一步
        long lt = new Long(s);
        Date date = new Date(lt * 1000);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间戳转换为时间
     *
     * s就是时间戳
     */

    public static String stampToDate(String s,String pattern) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        //如果它本来就是long类型的,则不用写这一步
        long lt = new Long(s);
        Date date = new Date(lt * 1000);
        res = simpleDateFormat.format(date);
        return res;
    }


    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 时间戳转换成字符窜
     *
     * @param milSecond
     * @return
     */
    public static String getDateToString(long milSecond) {
        // 10位的秒级别的时间戳
        String result1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(milSecond * 1000));
        System.out.println("10位数的时间戳（秒）--->Date:" + result1);
        Date date1 = new Date(milSecond * 1000);   //对应的就是时间戳对应的Date
        // 13位的秒级别的时间戳
        // double time2 = 1515730332000d;
        // String result2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time2);
        // System.out.println("13位数的时间戳（毫秒）--->Date:" + result2);


        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return result1;
    }

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsToday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsYesterday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }
    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }
    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();

    //判断选择的日期是否是本周
    public static boolean isThisWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(new Date(time));
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if (paramWeek == currentWeek) {
            return true;
        }
        return false;
    }

    /**
     *
     * @doc 日期转换星期几
     * @param datetime
     *            日期 例:2017-10-17
     * @return String 例:星期二
     * @history 2017年10月17日 上午9:55:30 Create by 【hsh】
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }





}
