package com.demo.common.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 参数配置明细
 * 
 * @author lihuan
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

	public enum type {
		STRING, INTEGER, DATE, DOUBLE, FLOAT, BOOLEAN, LONG, MOBILE, EMAIL
	}

	/**
	 * 参数名
	 */
	String name() default "";

	/**
	 * 是否必填
	 */
	boolean require() default false;

	/**
	 * 最大长度限制
	 */
	int maxLength() default -1;

	/**
	 * 最小长度限制
	 */
	int minLength() default -1;

	/**
	 * 参数类型
	 */
	type type() default type.STRING;

	/**
	 * 自定义正则表达式验证
	 */
	String regex() default "";

	/**
	 * 自定义错误提示
	 */
	String message() default "";

}
