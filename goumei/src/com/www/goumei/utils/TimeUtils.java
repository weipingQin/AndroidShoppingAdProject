package com.www.goumei.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 时间辅助类
 * @author Eric.Chen
 *
 */
public class TimeUtils {

	/**
	 * 获取当前时间戳
	 * 
	 * @return
	 */
	public static String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		return formatter.format(curDate);
	}
	/**
	 * 转时分秒显示xx:xx:xx
	 * @param time
	 * @return
	 */
	 public static String secToTime(int time) {  
		 	time=time/1000;
	        String timeStr = null;  
	        int hour = 0;  
	        int minute = 0;  
	        int second = 0;  
	        if (time <= 0)  
	            return "00:00";  
	        else {  
	            minute = time / 60;  
	            if (minute < 60) {  
	                second = time % 60;  
	                timeStr = unitFormat(minute) + ":" + unitFormat(second);  
	            } else {  
	                hour = minute / 60;  
	                if (hour > 99)  
	                    return "99:59:59";  
	                minute = minute % 60;  
	                second = time - hour * 3600 - minute * 60;  
	                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);  
	            }  
	        }  
	        return timeStr;  
	    }  
	  
	    public static String unitFormat(int i) {  
	        String retStr = null;  
	        if (i >= 0 && i < 10)  
	            retStr = "0" + Integer.toString(i);  
	        else  
	            retStr = "" + i;  
	        return retStr;  
	    }  
}
