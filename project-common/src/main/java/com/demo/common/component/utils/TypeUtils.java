package com.demo.common.component.utils;

import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 
 * 类型判断工具类
 * 
 * @author lihuan
 *
 */
public class TypeUtils {

	/**
	 * 检测字符串是否是int类型
	 * 
	 * @param str
	 *            要检测的字符串
	 * @return 如果str为int类型，返回<code>true</code>
	 */
	public static boolean isInt(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		try {
			Integer.valueOf(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 检测字符串是否是long类型
	 * 
	 * @param str
	 *            要检测的字符串
	 * @return 如果str为long类型，返回<code>true</code>
	 */
	public static boolean isLong(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		try {
			Long.valueOf(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 检测字符串是否是float类型
	 * 
	 * @param str
	 *            要检测的字符串
	 * @return 如果str为float类型，返回<code>true</code>
	 */
	public static boolean isFloat(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		try {
			Float.valueOf(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 检测字符串是否是double类型
	 * 
	 * @param str
	 *            要检测的字符串
	 * @return 如果str为double类型，返回<code>true</code>
	 */
	public static boolean isDouble(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		try {
			Double.valueOf(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 检测字符串是否是boolean类型
	 * 
	 * @param str
	 *            要检测的字符串
	 * @return 如果str为boolean类型，返回<code>true</code>
	 */
	public static boolean isBoolean(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		if (str.equals("true") || str.equals("false")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检测字符串是否是Date类型
	 * 
	 * @param str
	 *            要检测的字符串
	 * @return 如果str为Date类型，返回<code>true</code>
	 */
	public static boolean isDate(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		try {
			Date date = DateTools.parseDate(str);
			if (date != null) {
				return true;
			} else {
				return false;
			}
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
