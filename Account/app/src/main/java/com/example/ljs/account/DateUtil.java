package com.example.ljs.account;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ljs on 2017/5/28.
 */

public class DateUtil {
    //判断选择的日期是否是本周
    public static boolean isThisWeek(String time)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年mm月dd日");
        Date date = null;
        try {
            date = formatter.parse(time);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        long dateint=date.getTime();
        Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(new Date(dateint));
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if(paramWeek==currentWeek){
            return true;
        }
        return false;
    }

    //判断选择的日期是否是今天
    public static boolean isToday(String time)
    {
        return isThisTime(time,"yyyy年MM月dd日");
    }


    //判断选择的日期是否是本月
    public static boolean isThisMonth(String time)
    {
        return isThisTime(time,"yyyy年MM月");
    }

    //判断选择的日期是否是本年
    public static boolean isThisYear(String time)
    {
        return isThisTime(time,"yyyy年");
    }


    private static boolean isThisTime(String time,String pattern) {
        long nowtime = System.currentTimeMillis();
        Date date = new Date(nowtime);  //把当前时间转为date类型
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);  //设置格式
        String now = sdf.format(date);   //把当前时间转为string类型
        if(time.equals(now)){
            return true;
        }
        return false;
    }
}
