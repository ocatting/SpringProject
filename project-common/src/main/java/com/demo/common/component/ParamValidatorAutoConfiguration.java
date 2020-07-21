package com.demo.common.component;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Configuration
@EnableAspectJAutoProxy
public class ParamValidatorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ParamValidatorAnnotationAdvisor paramValidatorAnnotationAdvisor(){
        return new ParamValidatorAnnotationAdvisor(paramValidatorAnnotationInterceptor());
    }

    @Bean
    @ConditionalOnMissingBean
    public ParamValidatorAnnotationInterceptor paramValidatorAnnotationInterceptor(){
        return new ParamValidatorAnnotationInterceptor();
    }
}
