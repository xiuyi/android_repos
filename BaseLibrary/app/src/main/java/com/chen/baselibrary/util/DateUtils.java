package com.chen.baselibrary.util;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	private static final DateUtils instance = new DateUtils();
	private Calendar c;

	private DateUtils() {
		this.c = Calendar.getInstance();
	}

	public static DateUtils getInstance() {
		return instance;
	}

	/**
	 * 获取当前年
	 * 
	 * @return
	 */
	public int getCurrYear() {
		return c.get(Calendar.YEAR);
	}

	/**
	 * 获取当前月
	 * 
	 * @return
	 */
	public int getCurrMonth() {
		return c.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取当前日
	 * 
	 * @return
	 */
	public int getCurrDay() {
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取当前时
	 */
	public int getCurrHour() {
		return c.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取当前分
	 */
	public int getCurrMinute() {
		return c.get(Calendar.MINUTE);
	}

	/**
	 * 获取当前周
	 */
	public int getCurrWeek() {
		return c.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取当前周的中文标示
	 */
	public String getCurrWeekCNStr() {
		String[] weekDays = { "日", "一", "二", "三", "四", "五", "六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return weekDays[w];
	}

	/**
	 * 获取指定时间的年
	 * 
	 * @param date
	 * @return
	 */
	public int getDateYear(Date date) {
		return date.getYear();
	}

	/**
	 * 获取指定时间的月
	 * 
	 * @param date
	 * @return
	 */
	public int getDateMonth(Date date) {
		return date.getMonth();
	}

	/**
	 * 获取指定时间的日
	 * 
	 * @param date
	 * @return
	 */
	public int getDateDay(Date date) {
		return date.getDate();
	}

	/**
	 * 获取指定时间的时
	 * 
	 * @param date
	 * @return
	 */
	public int getDateHour(Date date) {
		return date.getHours();
	}

	/**
	 * 获取指定时间的分
	 * 
	 * @param date
	 * @return
	 */
	public int getDateMinute(Date date) {
		return date.getMinutes();
	}

	/**
	 * 获取指定日期的周中文标示
	 * 
	 * @param date
	 * @return
	 */
	public String getDateWeekCNStr(Date date) {
		String[] weekDays = { "日", "一", "二", "三", "四", "五", "六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return weekDays[w];
	}

	/**
	 * 获取now之后的时间
	 * 
	 * @param now
	 *            指定时间
	 * @param unit
	 *            指定Calendar的静态变量，来计算是之后的月、日、时、分、秒等信息
	 * @param value
	 *            之后的值
	 * @return 之后的时间值，如果value<0返回当前时间
	 */
	public Date getAfterDate(Date now, int unit, int value) {
		if (value < 0)
			return new Date();
		if (value == 0)
			return now;
		Calendar cacheC = Calendar.getInstance();
		cacheC.setTime(now);
		cacheC.add(unit, value);
		return cacheC.getTime();
	}

	/**
	 * 获取now之前的时间
	 * 
	 * @param now
	 *            指定时间
	 * @param unit
	 *            指定Calendar的静态变量，来计算是之前的月、日、时、分、秒等信息
	 * @param value
	 *            之前的值
	 * @return 之前的时间值，如果value<0返回当前时间
	 */
	public Date getBeforeDate(Date now, int unit, int value) {
		if (value < 0)
			return new Date();
		if (value == 0)
			return now;
		Calendar cacheC = Calendar.getInstance();
		cacheC.setTime(now);
		cacheC.add(unit, -value);
		return cacheC.getTime();
	}
	/**
	 * 判断指定日期是不是今天
	 * @param now
	 * @return
	 */
	public boolean isToday(Date now){
		return now.getDate()==new Date().getDate();
	}
	/**
	 * 判断指定日期是不是明天
	 * @param now
	 * @return
	 */
	public boolean isTomorrow(Date now){
		return now.getDate()==getAfterDate(new Date(), Calendar.DAY_OF_YEAR, 1).getDate();
	}
	/**
	 * 判断指定日期是不是昨天
	 * @param now
	 * @return
	 */
	public boolean isYesterday(Date now){
		return now.getDate()==getBeforeDate(new Date(), Calendar.DAY_OF_YEAR, 1).getDate();
	}

	/**
	 * 判断两个日期是不是同一天
	 */
	public boolean isSameDate(Date date1,Date date2){
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
				.get(Calendar.YEAR);
		boolean isSameMonth = isSameYear
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		boolean isSameDate = isSameMonth
				&& cal1.get(Calendar.DAY_OF_MONTH) == cal2
				.get(Calendar.DAY_OF_MONTH);
		return isSameDate;
	}
	/**
	 * 判断当前时间是早晨5-8 上午8-12 中午12-14 下午14-19 晚上19-24 凌晨0-5
	 */
	public String hourCNStr(Date now){
		int h = now.getHours();
		if(0<h&&h<=5)
			return "凌晨";
		else if(5<h&&h<=8)
			return "早晨";
		else if(8<h&&h<=12)
			return "上午";
		else if(12<h&&h<=14)
			return "中午";
		else if(14<h&&h<=19)
			return "下午";
		else if(19<h&&h<=24)
			return "晚上";
		return "";
	}
	/**
	 * 将时间格式化输出：如果是今天只显示HH:mm 如果是昨天就是 昨天 HH:mm 如果是明天就是 明天 HH:mm 其他的就是MM月dd日 HH:mm
	 * 包括小时和分钟
	 */
	public String formatDataTime(Date date){
		String format = "";
		if(date==null)
			return "";
		int minute = date.getMinutes();
		String HandM =  date.getHours()+":"+(minute>9?minute:("0"+minute));
		if(isToday(date)){
			format = hourCNStr(date)+HandM;
		}else if(isYesterday(date)){
			format = "昨天"+HandM;
		}else if(isTomorrow(date)){
			format = "明天"+HandM;
		}else{
			format = DateFormat.format("MM/dd",date)+" "+hourCNStr(date);
		}
		return format;
	}

	/**
	 * 格式化毫秒值为00:00:00 时:分:秒
	 * @return
	 */
    public String formatMilliSecond(long ms) {
		if(ms<=0){
			return "00:00";
		}else if(ms < 10000){
			return "00:0"+((int)ms/1000);
		}else if(ms < 60000){
			return "00:"+((int)ms/1000);
		}else{
			return ((int)ms/60000)+":"+(int)((ms-((int)ms/60000) * 60000) / 1000);
		}
    }
}
