package com.demo.common.component;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class ParamValidatorAnnotationAdvisor extends AbstractPointcutAdvisor implements
        BeanFactoryAware {

    private Advice advice;

    private Pointcut pointcut;

    public ParamValidatorAnnotationAdvisor(@NotNull ParamValidatorAnnotationInterceptor paramValidatorAnnotationInterceptor) {
        this.advice = paramValidatorAnnotationInterceptor;
        this.pointcut = buildPointcut();
    }

    @Override
    public Pointcut getPointcut() {
        Assert.notNull(this.pointcut, "'pointcut' must not be null");
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        Assert.notNull(this.advice, "'advice' must not be null");
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    private Pointcut buildPointcut() {
        Pointcut cpc = new AnnotationMatchingPointcut(ParamValidator.class, true);
        Pointcut mpc = AnnotationMatchingPointcut.forMethodAnnotation(ParamValidator.class);
        return new ComposablePointcut(cpc).union(mpc);
    }
}
