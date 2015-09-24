package com.www.goumei.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;



import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Util {

    public static String getDate(String str) {
        String str_return = "";
        if (str != null && str.length() == 8) {
            String yy = str.substring(0, 4) + "年";
            String mm = str.substring(4, 6) + "月";
            String dd = str.substring(6) + "日";
            str_return = yy + mm + dd;
        }
        return str_return;
    }

  

    public static String getErrorInfoFromException(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return "\r\n" + sw.toString() + "\r\n";
        } catch (Exception e2) {
            return "bad getErrorInfoFromException";
        }
    }

    /**
     * 判断字符串是否全是中文
     *
     * @param str
     * @return
     */
    public static boolean isAllChinese(String str) {
        for (int i = 0; i < str.length(); i++) {
            char ss = str.charAt(i);
            boolean s = String.valueOf(ss).matches("[\u4e00-\u9fa5]");
            if (!s) {
                return false;
            }
        }
        return true;
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }


    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }


    //获取到期时间
    public static String getDueTime(String birthday) {
        if (StringUtils.isEmpty(birthday)) {
            return "";
        }

        String result = "";
        if (getOffsetDay(birthday, getToday()) > 365) {
            String yy = birthday.substring(0, 4);
            String mm = birthday.substring(4, 6);
            String dd = birthday.substring(6);
            String tempday = getYear() + mm + dd;
            if (getOffsetDay(tempday, Util.getToday()) > 0) {
                result = (parseInt(Util.getYear()) + 1) + "-" + mm + "-" + dd;
            } else {
                result = getDate1(tempday);
            }

        } else {
            result = getOneYearLater(birthday);
        }
        return result;
    }


    public static String isRemindTime(String time) {
        if (StringUtils.isEmpty(time)) {
            return "";
        }

        String dueTime = getDueTime(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(toDateFromYYYY_MM_DD((dueTime)));
        calendar.add(Calendar.DATE, -30);
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        String remindTm = format.format(calendar.getTime());
        String result = "";
        int offDays = Integer.parseInt(getOffsetDay(remindTm, getToday()) + "");
        if (offDays > 0 && offDays < 30) {
            result = dueTime;
        } else {
            result = "";
        }
        return result;
    }


    public static String getEntrustOrders(int orderid, String orderno) {
        String result = "";
        if (StringUtils.isEmpty(orderno)) {
            result = orderid + "";
        } else {
            result = orderno;
        }
        return result;

    }

    public static String getDateByStr(String strdate) {
        DateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        DateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String mydate = "";
        if (strdate != null && !strdate.equals("")) {
            try {
                mydate = fmt2.format(fmt.parse((strdate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return mydate;
    }
    public static String getDateHHmm(String strdate) {
    	DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	DateFormat fmt2 = new SimpleDateFormat("MM-dd HH:mm");
    	String mydate = "";
    	if (strdate != null && !strdate.equals("")) {
    		try {
    			mydate = fmt2.format(fmt.parse((strdate)));
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
    	}
    	return mydate;
    }
    public static String getDateMMdd(String strdate) {
    	DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	DateFormat fmt2 = new SimpleDateFormat("MM-dd");
    	String mydate = "";
    	if (strdate != null && !strdate.equals("")) {
    		try {
    			mydate = fmt2.format(fmt.parse((strdate)));
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
    	}
    	return mydate;
    }

    public static String getTireUseTm(String createTm) {
        String useTm = "";
        if (createTm == null || createTm.length() != 4) {
            return useTm;
        }
        StringBuilder sb = new StringBuilder();
        int zhou = parseInt(createTm.substring(0, 2));
        int nian = parseInt(createTm.substring(2, 4));

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(now);
        int nowWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        int nowYear = calendar.get(Calendar.YEAR);
        Log.i("info_out", "nowYear:" + nowYear + "week:" + nowWeek);
        if (nian > 50) {
            nian = nian + 1900;
        } else {
            nian = nian + 2000;
        }
        int useYear = nowYear - nian - 1;
        int pluszhou = 0;
        if (zhou < 52) {
            pluszhou = 52 - zhou;
        }
        int useWeek = pluszhou + nowWeek;
        while (useWeek > 52) {
            useWeek = useWeek - 52;
            useYear++;
        }

        if (useYear < 0) {
            return "";
        }

        sb.append("已使用");
        sb.append(useYear);
        sb.append("年");
        sb.append(useWeek);
        sb.append("周");

        useTm = sb.toString();
        return useTm;

    }

  

    public static int getMyResultPrice(float discount) {
        int result = 0;
        if ((int) Math.floor(discount) >= 0) {
            result = (int) Math.floor(discount);
        }


        return result;
    }

    public static int priceFormart2(Float price){
		if(price == null){
			return 0;
		}
		BigDecimal decimalPrice = new BigDecimal(price);
		decimalPrice.setScale(0, BigDecimal.ROUND_DOWN);
		
		System.out.println(decimalPrice.toBigInteger().toString());
		return decimalPrice.toBigInteger().intValue();
	}
    
    public static String getDoubleTwoFloat(float discount) {
        float result = 0f;
        result = (float) (Math.round(discount * 100)) / 100;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(result);
    }

    public static String getDoubleTwoFloatPlus(float discount, float discountAmount) {

        Float price = discount < discountAmount ? discountAmount : discount;

        float result = 0f;
        result = (float) (Math.round(price * 100)) / 100;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(result);
    }

    public static String getDoubleTwoItem1Float(float discount) {
        float result = 0f;
        String ret = "";
        if (discount == 0f) {
            ret = "0.00";
        } else {
            result = (float) (Math.round(discount * 100)) / 100;
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            ret = decimalFormat.format(result);
        }

        return ret;
    }

    public static String getDoubleTwoYouhuiFloat(float youhui) {
        float result = 0f;
        String ret = "";
        if (youhui <= 0f) {
            ret = "";
        } else {
            result = (float) (Math.round(youhui * 100)) / 100;
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            ret = decimalFormat.format(result) + "元";
        }

        return ret;
    }
    public static String getDoubleTwoDaZheFloat(float dazhe) {
    	float result = 0f;
    	String ret = "";
    	if (dazhe <= 0f) {
    		ret = "";
    	} else {
    		result = (float) (Math.round(dazhe * 100)) / 100;
    		DecimalFormat decimalFormat = new DecimalFormat("0.0");
    		ret = decimalFormat.format(result) + "折";
    	}
    	
    	return ret;
    }

    public static String getDoubleTwoItemFloat(float discount, float dep) {
        String ret = "";
        float result = 0f;
        if (dep == 100 && discount == 0) {
            ret = "时价";
        } else {
            result = discount * dep / 100;
            result = (float) (Math.round(result * 100)) / 100;
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            ret = decimalFormat.format(result);
        }

        return ret;
    }

    
    public static String getDate1(String str) {
        String str_return = "";
        if (str != null && str.length() == 8) {
            String yy = str.substring(0, 4) + "-";
            String mm = str.substring(4, 6) + "-";
            String dd = str.substring(6);
            str_return = yy + mm + dd;
        }
        return str_return;
    }
    public static String getDateBySplit1(String str) {
        String str_return = "";
        if (str != null && str.length() == 8) {
            String yy = str.substring(0, 4) + "/";
            String mm = str.substring(4, 6) + "/";
            String dd = str.substring(6);
            str_return = yy + mm + dd;
        }
        return str_return;
    }
    public static String getDateBySplit(String str) {
        String str_return = "";
        if (str != null && str.length() == 8) {
            String mm = str.substring(4, 6) + "/";
            String dd = str.substring(6);
            str_return = mm + dd;
        }
        return str_return;
    }

    public static String getDate3(String str) {
        String str_return = "";
        if (str != null) {
            str_return = str;
        }
        return str_return;
    }

    public static int getTodayMiles(String eoFactoryDt, String recordtm, int recordmiles, int miles1, int miles2) {

        int result = 0;
        if (recordtm.equals(getToday())) {
            return recordmiles;
        }
        if (eoFactoryDt != null && eoFactoryDt.equals(recordtm))
            return recordmiles;


        long day = (getOffsetDay(recordtm, getToday()));
        int day1 = (int) day;
        if (miles2 > 0) {


            result = recordmiles + day1 * miles2 / 30;

        } else if (miles1 > 0) {
            result = recordmiles + day1 * miles1 / 30;


        }


        return result;
    }

    public static int parseInt(String intStr) {
        int result = 0;
        try {
            result = Integer.parseInt(intStr);
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public static float parseFloat(String floatStr) {
        float result = 0f;
        try {
            result = Float.parseFloat(floatStr);
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public static double parseDouble(String floatStr) {
        double result = 0f;
        try {
            result = Double.parseDouble(floatStr);
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public static String getTime1(String str) {
        String str_return = "";
        if (str != null && str.length() == 6) {
            String yy = str.substring(0, 2) + ":";
            String mm = str.substring(2, 4) + ":";
            String dd = str.substring(4);
            str_return = yy + mm + dd;
        }
        return str_return;
    }

    public static boolean isEmpty(final String str) {
        if (str == null) {
            return true;
        }
        if (str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static String stringFill3(String source, int fillLength, char fillChar, boolean isLeftFill) {
        if (source == null) return source;

        if (source.length() >= fillLength) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < source.length(); i += fillLength) {
                if ((i + fillLength) > source.length()) {
                    sb.append(source.substring(i, source.length()));
                } else if ((i + fillLength) < source.length()) {
                    sb.append(source.substring(i, i + fillLength));
                    sb.append("\n");
                } else {
                    sb.append(source.substring(i, i + fillLength));
                }
            }
            return sb.toString();
        } else {
            char[] c = new char[fillLength];
            char[] s = source.toCharArray();
            int len = s.length;
            if (isLeftFill) {
                int fl = fillLength - len;
                for (int i = 0; i < fl; i++) {
                    c[i] = fillChar;
                }
                System.arraycopy(s, 0, c, fl, len);
            } else {
                System.arraycopy(s, 0, c, 0, len);
                for (int i = len; i < fillLength; i++) {
                    c[i] = fillChar;
                }
            }
            return String.valueOf(c);
        }


    }


    public static String stringFill2(String source, int fillLength, char fillChar, boolean isLeftFill) {
        if (source == null) return source;

        if (source.length() >= fillLength) {

            return source.substring(0, fillLength - 1) + "…";
        } else {
            char[] c = new char[fillLength];
            char[] s = source.toCharArray();
            int len = s.length;
            if (isLeftFill) {
                int fl = fillLength - len;
                for (int i = 0; i < fl; i++) {
                    c[i] = fillChar;
                }
                System.arraycopy(s, 0, c, fl, len);
            } else {
                System.arraycopy(s, 0, c, 0, len);
                for (int i = len; i < fillLength; i++) {
                    c[i] = fillChar;
                }
            }
            return String.valueOf(c);
        }


    }

    //半年
    public static String getHalfYearStart() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String startdate = "";
        if (1 <= month && month <= 6) {
            startdate = String.valueOf(year) + "0101";
        }
        if (7 <= month && month <= 12) {
            startdate = String.valueOf(year) + "0701";
        }
        return startdate;
    }


    //当季首日
    public static String getQuarterStart() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String startdate = "";
        if (1 <= month && month <= 3) {
            startdate = String.valueOf(year) + "0101";
        }
        if (4 <= month && month <= 6) {
            startdate = String.valueOf(year) + "0401";
        }
        if (7 <= month && month <= 9) {
            startdate = String.valueOf(year) + "0701";
        }
        if (10 <= month && month <= 12) {
            startdate = String.valueOf(year) + "1001";
        }
        return startdate;
    }

    //当季尾日
    public static String getQuarterEnd() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String startdate = "";
        if (1 <= month && month <= 3) {
            startdate = String.valueOf(year) + "0331";
        }
        if (4 <= month && month <= 6) {
            startdate = String.valueOf(year) + "0630";
        }
        if (7 <= month && month <= 9) {
            startdate = String.valueOf(year) + "0930";
        }
        if (10 <= month && month <= 12) {
            startdate = String.valueOf(year) + "1231";
        }
        return startdate;
    }

    //---------返回当月首日-----------
    public static String getFirstDayOfMonth() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        DateFormat simpleFormate = new SimpleDateFormat("yyyyMMdd");
        String sd = simpleFormate.format(calendar.getTime());
        return sd;
    }

    //---------返回当月尾日-----------
    public static String getLastDayOfMonth() {
        Calendar calendar = new GregorianCalendar();
        DateFormat simpleFormate = new SimpleDateFormat("yyyyMMdd");
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String ed = simpleFormate.format(calendar.getTime());
        return ed;
    }

    //  本周开始日
    public static String getStartweek() {

        Calendar calender = Calendar.getInstance();

        int week = calender.get(Calendar.DAY_OF_WEEK);

        int offset = 0;
        if (week == 1) {
            offset = -6;
        } else if (week == 2) {
            offset = 0;
        } else {
            offset = 2 - week;
        }

        calender.add(Calendar.DAY_OF_MONTH, offset);

        return Util.toStrDateYYYYMMDD(calender.getTime());
    }


    //本周终了日
    public static String getEndweek() {

        Calendar calender = Calendar.getInstance();

        int week = calender.get(Calendar.DAY_OF_WEEK);

        int offset = 0;
        if (week == 1) {
            offset = 0;
        } else if (week == 2) {
            offset = 6;
        } else {
            offset = 8 - week;
        }

        calender.add(Calendar.DAY_OF_MONTH, offset);

        return Util.toStrDateYYYYMMDD(calender.getTime());
    }


    public static String getToday() {
        Date today1 = new Date();
        DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        String mydate = fmt.format(today1);
        return mydate;
    }

    public static String getYear() {
        Date today1 = new Date();
        DateFormat fmt = new SimpleDateFormat("yyyy");
        String mydate = fmt.format(today1);
        return mydate;
    }

    public static String getToday1() {
        Date today1 = new Date();
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String mydate = fmt.format(today1);
        return mydate;
    }

    /**
     * 得到现在小时
     */
    public static String getHour() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String hour;
        hour = dateString.substring(11, 13);
        return hour;
    }

    /**
     * 得到现在分钟
     */
    public static String getMinute() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String minute;
        minute = dateString.substring(14, 16);
        return minute;
    }

    public static String getHourMinute() {
        Date today1 = new Date();
        DateFormat fmt = new SimpleDateFormat("HH:mm");
        String mydate = fmt.format(today1);
        return mydate;
    }

    public static String getDateDB(String strdate) {
        DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        DateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd");
        String mydate = "";
        if (strdate != null && !strdate.equals("")) {
            try {
                mydate = fmt.format(fmt2.parse((strdate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return mydate;
    }

    public static String getDateDBRet(String strdate) {
        DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        DateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd");
        String mydate = "";
        if (strdate != null && !strdate.equals("")) {
            try {
                mydate = fmt2.format(fmt.parse((strdate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return mydate;
    }

    public static String getTimeDBRet(String strdate) {
        DateFormat fmt = new SimpleDateFormat("HH:mm:ss");
        DateFormat fmt2 = new SimpleDateFormat("HHmmss");
        String mydate = "";
        if (strdate != null && !strdate.equals("")) {
            try {
                mydate = fmt.format(fmt2.parse((strdate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return mydate;
    }

    public static Integer getAge(String birthday) throws ParseException {
        Integer age = 0;
        if (birthday != null && birthday.length() == 8) {
            DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
            Date today = new Date();
            Date birth = fmt.parse(birthday);
            long day = (today.getTime() - birth.getTime()) / (24 * 60 * 60 * 1000) + 1;
            age = (int) (day / 365f);
        }
        return age;
    }

    //---------根据  HcPurchaseItem 状态 返回 字符串---------
    public static String getHcPurchaseItemState(Integer flag) {
        String strState = "";
        if (flag != null) {
            if (flag == 0)
                strState = "<font color='#FF0000'>未入库</font>";
            else if (flag == 1)
                strState = "<font color='#00FF00'>已入库</font>";
        }

        return strState;
    }


    public static String getPercent(double data) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        return nf.format(data);
    }

    public static Date getDateByMonths(int months, Date baseDate) {
        Calendar l_cal = Calendar.getInstance();
        l_cal.setTime(new Date());
        l_cal.add(Calendar.MONTH, months);
        Date l_date = l_cal.getTime();
        return l_date;
    }

    public static String toStrDateYYYYMMDD(final Date date) {
        SimpleDateFormat l_format = (SimpleDateFormat) DateFormat.getInstance();
        l_format.applyPattern("yyyyMMdd");
        return toStrDate(date, l_format);
    }

    public static String toStrDateYYYYMMDD2(final Date date) {
        SimpleDateFormat l_format = (SimpleDateFormat) DateFormat.getInstance();
        l_format.applyPattern("yyyy-MM-dd");
        return toStrDate(date, l_format);
    }

    public static String toStrDate(final Date date,
                                   final SimpleDateFormat format) {
        if (date == null) {
            return null;
        }
        return format.format(date);
    }

    public static Date getCurDate() {
        Date l_date = new Date();
        return l_date;
    }

    //获取昨天的日期
    public static Date getDateOfYesterday() {
        Calendar c = Calendar.getInstance();
        long t = c.getTimeInMillis();
        long l = t - 24 * 3600 * 1000;
        Date d = new Date(l);
        return d;
    }

    public static String toDateDb(final String dateInput, final String type) {
        String l_dateDb = "";
        if (!isEmpty(dateInput)) {
            String[] l_date = dateInput.split(type);
            l_dateDb = l_date[0];
            if (!isEmpty(l_date[1]) && l_date[1].length() == 1) {
                l_dateDb += "0";
            }
            l_dateDb += l_date[1];
            if (!isEmpty(l_date[2]) && l_date[2].length() == 1) {
                l_dateDb += "0";
            }
            l_dateDb += l_date[2];
        }
        return l_dateDb;
    }

    public static String toDateDb2(final String dateInput, final String type) {
        String l_dateDb = "";
        if (!isEmpty(dateInput)) {
            String[] l_date = dateInput.split(type);
            l_dateDb = l_date[2];
            if (!isEmpty(l_date[1]) && l_date[1].length() == 1) {
                l_dateDb += "0";
            }
            l_dateDb += l_date[1];
            if (!isEmpty(l_date[0]) && l_date[0].length() == 1) {
                l_dateDb += "0";
            }
            l_dateDb += l_date[2];
        }
        return l_dateDb;
    }

    public static String toStrDateYYYYMMDD3(final String dateInput, final String type) {
        String l_date = "";
        if (!isEmpty(dateInput) && dateInput.length() == 8) {
            l_date = dateInput.substring(0, 4) + type + dateInput.substring(4, 6) + type + dateInput.substring(6);
        }
        return l_date;
    }

    public static String toStrDateYYYYMMDD4(final String dateInput, final String type) {
        String l_date = "";
        if (!isEmpty(dateInput) && dateInput.length() == 8) {
            l_date = dateInput.substring(6) + type + dateInput.substring(4, 6) + type + dateInput.substring(0, 4);
        }
        return l_date;
    }

    public static String toStrDateHHMMSS(final String dateInput, final String type) {
        String l_date = "";
        if (!isEmpty(dateInput) && dateInput.length() == 6) {
            l_date = dateInput.substring(0, 2) + type + dateInput.substring(2, 4) + type + dateInput.substring(4);
        }
        return l_date;
    }

    public static String toStrDateHHMM(final String dateInput, final String type) {
        String l_date = "";
        if (!isEmpty(dateInput) && dateInput.length() == 4) {
            l_date = dateInput.substring(0, 2) + type + dateInput.substring(2, 4);
        }
        return l_date;
    }

    public static long getDays(final String dateFrom, final String dateTo) {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            return new Long((format.parse(dateFrom).getTime() - format.parse(
                    dateTo).getTime())
                    / (1000 * 60 * 60 * 24) + 1).intValue();
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String toStrDateHHMMSS(final Date date) {
        SimpleDateFormat l_format = (SimpleDateFormat) DateFormat.getInstance();
        l_format.applyPattern("HHmmss");
        return toStrDate(date, l_format);
    }

    public static String toStrDateHHMMSS2(final Date date) {
        SimpleDateFormat l_format = (SimpleDateFormat) DateFormat.getInstance();
        l_format.applyPattern("HH:mm:ss");
        return toStrDate(date, l_format);
    }

    public static Date toDateFromYYYYMMDD(String strDate) {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        try {
            date = format.parse(strDate); // Thu Jan 18 00:00:00 CST 2007
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date toDateFromYYYY_MM_DD(String strDate) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = format.parse(strDate); // Thu Jan 18 00:00:00 CST 2007
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getNowYYYYMMDDHHMMSS() {
        String result = "";
        SimpleDateFormat l_sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        result = l_sdf.format(new Date());
        return result;
    }

    public static String getNowYYYYMMDDHHMM() {
        String result = "";
        SimpleDateFormat l_sdf = new SimpleDateFormat("yyyyMMddHHmm");
        result = l_sdf.format(new Date());
        return result;
    }

    public static String getEntryDtTm2() {
        String result = "";
        SimpleDateFormat l_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        result = l_sdf.format(new Date());
        return result;
    }

    public static String getCreateTime() {
        String result = "";
        SimpleDateFormat l_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        result = l_sdf.format(new Date());
        return result;
    }

    public static String getNowTm() {
        SimpleDateFormat l_sdf = new SimpleDateFormat("HHmmss");
        return l_sdf.format(new Date());
    }

    public static String getNowTm1() {
        SimpleDateFormat l_sdf = new SimpleDateFormat("HH:mm:ss");
        return l_sdf.format(new Date());
    }

    public static String getStrDateForYYYYMMDDHHMMSS(Date date) {
        String result = "";
        if (date == null) {
            return result;
        }
        SimpleDateFormat l_sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        result = l_sdf.format(date);
        return result;
    }

    public static String getStartTimeByMonth(Date date) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.set(Calendar.DAY_OF_MONTH, 1);
        calender.set(Calendar.HOUR_OF_DAY, 0);
        calender.set(Calendar.MINUTE, 0);
        calender.set(Calendar.SECOND, 0);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(calender.getTime());
    }

    public static String getEndTimeByMonth(Date date) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.MONTH, 1);
        calender.set(Calendar.DAY_OF_MONTH, 1);
        calender.add(Calendar.DAY_OF_MONTH, -1);
        calender.set(Calendar.HOUR_OF_DAY, 23);
        calender.set(Calendar.MINUTE, 59);
        calender.set(Calendar.SECOND, 59);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(calender.getTime());
    }

    public static String getStartDateByMonth(Date date) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(calender.getTime());
    }

    public static String getDateByTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date.getTime());
    }

    public static String get00fomate(int time) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(time);
    }

    public static String getEndDateByMonth(Date date) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.MONTH, 1);
        calender.set(Calendar.DAY_OF_MONTH, 1);
        calender.add(Calendar.DAY_OF_MONTH, -1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(calender.getTime());
    }

    public static Calendar getEndCalByMonth(Date date) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.MONTH, 1);
        calender.set(Calendar.DAY_OF_MONTH, 1);
        calender.add(Calendar.DAY_OF_MONTH, -1);
//    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return calender;
    }

    public static String floatTrans(float d) {
        if (Math.round(d) - d == 0) {
            return String.valueOf((int) d);
        }
        return String.valueOf(d);
    }

    public static Integer convertToInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static Long getOffsetDay(String startDate, String endDate) {
        Date start = toDateFromYYYYMMDD(startDate);
        Date end = toDateFromYYYYMMDD(endDate);
        long offset = end.getTime() - start.getTime();
        return (offset / (24 * 60 * 60 * 1000));
    }

    public static String getOneYearLater(String time) {
        String yy = time.substring(0, 4);
        String mm = time.substring(4, 6);
        String dd = time.substring(6);

        int nowYY = Integer.parseInt(yy) + 1;
        return nowYY + "-" + mm + "-" + dd;

        /*Log.i("info_out","time"+time);
        Date start = toDateFromYYYYMMDD(time);
       long resultTime = start.getTime()+365*24*60*60*1000;
        Date resultDt = new Date(resultTime);
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(resultDt);*/
    }

    public static String toStrDateYYYYMMDD5(final String date, String format) {
        if (Util.isEmpty(date)) {
            return "";
        }
        SimpleDateFormat l_format = new SimpleDateFormat(format);
        DateFormat l_dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return l_dateFormat.format(l_format.parse(date));
        } catch (ParseException e) {
            return "";
        }
    }

    public static String getDateYYYYMMDDHHMM(String strdate) {
        DateFormat fmt = new SimpleDateFormat("yyyyMMddHHmm");
        DateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String mydate = "";
        if (strdate != null && !strdate.equals("")) {
            try {
                mydate = fmt2.format(fmt.parse((strdate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return mydate;
    }

    public static String getDateYYYYMMDDHHMM2(String strdate) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DateFormat fmt2 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        String mydate = "";
        if (strdate != null && !strdate.equals("")) {
            try {
                mydate = fmt.format(fmt2.parse((strdate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return mydate;
    }

    public static String getDateYYYYMMDDHHMMRet(String strdate) {
        DateFormat fmt = new SimpleDateFormat("yyyyMMddHHmm");
        DateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String mydate = "";
        if (strdate != null && !strdate.equals("")) {
            try {
                mydate = fmt.format(fmt2.parse((strdate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return mydate;
    }

    public static String getEntryDtTm(String strdate) {
        DateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        DateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String mydate = "";
        if (!StringUtils.isEmpty(strdate)) {
            try {
                mydate = fmt2.format(fmt.parse(strdate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return mydate;
    }

    public static Date toDateFromYYYYMMDDHHMMSS(String strDate) {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        try {
            date = format.parse(strDate); // Thu Jan 18 00:00:00 CST 2007
        } catch (ParseException e) {
            date = null;
        }
        return date;
    }

    public static String getStandCarplate(String licesencePath) {
        String result = licesencePath.replace("O", "0");

        char[] crs = result.toCharArray();

        if (crs[1] == '0') {
            crs[1] = 'O';
        }

        return new String(crs);
    }

  /*  public static String priceFormart(Float price) {
        if (price == null) {
            return "0.00";
        }
        String priceStr = price.toString();
        BigDecimal decimalPrice = new BigDecimal(priceStr);
        DecimalFormat df1 = new DecimalFormat("##,##0.00");
        return (df1.format(decimalPrice));
    }*/

    public static String priceFormart3(Float price) {
        if (price == null) {
            return "0.00";
        }
        String priceStr = price.toString();
        BigDecimal decimalPrice = new BigDecimal(priceStr);
        DecimalFormat df1 = new DecimalFormat("#0.00");
        return (df1.format(decimalPrice));
    }
    
    public static Double convertToDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return 0d;
        }
    }

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|" +
                "(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }


    public static boolean checkNet(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connManager != null) {
                NetworkInfo info = connManager.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }

        return false;
    }

    //取消指针
    public static Object deepCopy(Object obj) {      
    	try{
    	       ByteArrayOutputStream bos = new ByteArrayOutputStream();  
    	       
    	        ObjectOutputStream oos = new ObjectOutputStream(bos);  
    	  
    	        oos.writeObject(obj);  
    	  
    	        //将流序列化成对象  
    	        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());  
    	  
    	        ObjectInputStream ois = new ObjectInputStream(bis);  
    	  
    	        return ois.readObject();  
    	} catch(Exception e){
    	 e.printStackTrace();
    	}
    	return null;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param context
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param context
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param context
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param context
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
   
    public static boolean isMobileNO(String mobiles) {
    	Pattern p = Pattern
    	.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
    	Matcher m = p.matcher(mobiles);

    	return m.matches();
    	}
    public static long getAvailMemory(Context context) {// 获取android当前可用内存大小 

    	ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    	MemoryInfo mi = new MemoryInfo();
    	am.getMemoryInfo(mi);
    	//mi.availMem; 当前系统的可用内存 
    	
    	return mi.availMem;
    	}

    public static  long getTotalMemory() {
    	String str1 = "/proc/meminfo";// 系统内存信息文件 
    	String str2;
    	String[] arrayOfString;
    	long initial_memory = 0;

    	try {
    	FileReader localFileReader = new FileReader(str1);
    	BufferedReader localBufferedReader = new BufferedReader(
    	localFileReader, 8192);
    	str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小 

    	arrayOfString = str2.split("\\s+");
    	for (String num : arrayOfString) {
    	Log.i(str2, num + "\t");
    	}

    	initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte 
    	localBufferedReader.close();

    	} catch (IOException e) {
    	}
    	return  initial_memory;
    	}
    /**
     * 根据系统时间获得指定位数的随机数
     * 
     * @return 获得的随机数
     */
    public static String getRandom() {
       String numberChar = "0123456789";
        Long seed = System.currentTimeMillis();// 获得系统时间，作为生成随机数的种子
 
        StringBuffer sb = new StringBuffer();// 装载生成的随机数
 
        Random random = new Random(seed);// 调用种子生成随机数
 
        for (int i = 0; i < 5; i++) {
 
            sb.append(numberChar.charAt(random.nextInt(numberChar.length())));
        }
 
        return sb.toString();
 
    }
    /** 
    * 判断日期格式是否正确 
    */  
   public static boolean IsDateFormat(String dataStr) {  
       boolean state = false;  
       try {  
          SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");   
           dFormat.setLenient(false);   
           java.util.Date d = dFormat.parse(dataStr);  
           state = true;  
       } catch (ParseException e) {  
           e.printStackTrace();  
           state = false;  
       }   
       return state;  
   }
   /** 
    * 判断日期格式是否正确 
    */  
   public static boolean IsDateFormat2(String dataStr) {  
	   boolean state = false;  
	   try {  
		   SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd");   
		   dFormat.setLenient(false);   
		   java.util.Date d = dFormat.parse(dataStr);  
		   state = true;  
	   } catch (ParseException e) {  
		   e.printStackTrace();  
		   state = false;  
	   }   
	   return state;  
   }
   /**
    * 显示  网络图片
    * @param url
    * @return
    */
   /** 
    * Get image from newwork 
    * @param path The path of image 
    * @return InputStream 
    * @throws Exception 
    */  
   public static InputStream getImageStream(String path) throws Exception{  
       URL url = new URL(path);  
       HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
       conn.setConnectTimeout(5 * 1000);  
       conn.setRequestMethod("GET");  
       if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){  
           return conn.getInputStream();  
       }  
       return null;  
   }  
   /** 
    * Get data from stream 
    * @param inStream 
    * @return byte[] 
    * @throws Exception 
    */  
   public static byte[] readStream(InputStream inStream) throws Exception{  
       ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
       byte[] buffer = new byte[1024];  
       int len = 0;  
       while( (len=inStream.read(buffer)) != -1){  
           outStream.write(buffer, 0, len);  
       }  
       outStream.close();  
       inStream.close();  
       return outStream.toByteArray();  
   }  

	/**
	 * 用异常来做校验字符串是否是整数
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}