package com.demo.common.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 参数验证器
 * 
 * @author lihuan
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamValidator {

	/**
	 * 参数集合配置明细
	 * 
	 * @return
	 */
	Param[] parameters() default {};

}
