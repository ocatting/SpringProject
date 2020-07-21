package com.demo.common.component.utils;

public class RegexUtils {

	/**
	 * 电话号码正则表达式
	 */
	public static String telRegex = "^1[0-9]{10}$";
	/**
	 * 邮箱正则表达式
	 */
	public static String emailRegex = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

	/** 正则表达试验证手机号 */
	public static boolean checkMobile(String value) {
		return (value.matches(telRegex));
	}

	/** 正则表达试验证邮箱号 */
	public static boolean checkEmail(String value, int length) {
		if (length == 0) {
			length = 40;
		}
		return (value.matches(emailRegex)) && (value.length() <= length);
	}

}
