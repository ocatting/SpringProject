package com.demo.common.component.utils;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTools {

	/**
	 * 将字符串类型的日期转换为java.util.Date类型
	 * 
	 * @param datetime
	 *            要转换的字符串日期
	 * @return
	 */
	public static Date parseDate(String datetime) {
		if (StringUtils.isEmpty(datetime)) {
			return null;
		}
		String[] pattern = new String[] { "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyyMMdd", "yyyy-MM-dd", "yyyy/MM/dd",
				"yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss" };
		try {
			return DateUtils.parseDate(datetime, pattern);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取今天日期
	 * 
	 * @param format
	 *            日期返回格式,默认(传递null)为yyyy-MM-dd hh:mm:ss
	 * @return
	 */
	public static String getNow(String format) {
		if (format == null)
			format = "yyyy-MM-dd hh:mm:ss";
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		Date d = ca.getTime();
		DateFormat dfromat = new SimpleDateFormat(format);
		String date = dfromat.format(d);
		return date;
	}

	/**
	 * 格式化日期，返回yyyy-MM-dd hh:mm:ss格式的时间字符串
	 * 
	 * @param date
	 *            待格式化的日期
	 * @return
	 */
	public static String format(Date date) {
		return format(date, "yyyy-MM-dd hh:mm:ss");
	}

	public static String format(String date) throws ParseException {
		return format(date, "yyyy-MM-dd hh:mm:ss");
	}

	public static String format(String date, String format) throws ParseException {
		if (StringUtils.isEmpty(date)) {
			return null;
		}
		DateFormat dfromat = new SimpleDateFormat(format);
		return dfromat.format(dfromat.parse(date));
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 *            待格式化的日期
	 * @param format
	 *            日期返回格式,默认(传递null)为yyyy-MM-dd hh:mm:ss
	 * @return
	 */
	public static String format(Date date, String format) {
		if (format == null)
			format = "yyyy-MM-dd hh:mm:ss";
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		Date d = ca.getTime();
		DateFormat dfromat = new SimpleDateFormat(format);
		return dfromat.format(d);
	}

	/**
	 * 
	 * 获取当前时间的前一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getPrevDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 
	 * 获取当前时间的后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getNextDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		date = calendar.getTime();
		return date;
	}

	@SuppressWarnings("deprecation")
	public static String parseToTimestamp(String datetime) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date(datetime));
		return String.valueOf(ca.getTimeInMillis());
	}

	public static Date getTodayStart() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date sdate = calendar.getTime();
		return sdate;
	}

	public static Date getTodayEnd() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 24);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.MILLISECOND, -1);
		Date edate = calendar.getTime();
		return edate;
	}


}
