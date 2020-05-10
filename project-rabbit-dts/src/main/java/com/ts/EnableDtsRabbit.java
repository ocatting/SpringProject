package com.ts;

import com.ts.config.RabbitDtsConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@Import(RabbitDtsConfiguration.class)
public @interface EnableDtsRabbit {
}
