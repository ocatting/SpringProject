package com.demo.login.config;

import com.demo.login.config.properties.NoAuthUrlProperties;
import com.demo.login.intercepter.AuthInterceptorHandler;
import com.demo.login.intercepter.JwtInterceptorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
/**
 * @Description: 对web 的配置，可过滤请求，配置首页等
 * @Author Yan XinYu
 **/
@Configuration
@EnableConfigurationProperties(NoAuthUrlProperties.class)
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Autowired
    private NoAuthUrlProperties noAuthUrlProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Session
//        registry.addInterceptor(authInterceptorHandler())
//                .addPathPatterns("/**")
//                .excludePathPatterns(new ArrayList<>(noAuthUrlProperties.getShouldSkipUrls()));
        // JWT
        registry.addInterceptor(jwtInterceptorHandler())
                .addPathPatterns("/**")
                .excludePathPatterns(new ArrayList<>(noAuthUrlProperties.getShouldSkipUrls()));


    }

    @Bean
    public AuthInterceptorHandler authInterceptorHandler(){
        return new AuthInterceptorHandler();
    }

    @Bean
    public JwtInterceptorHandler jwtInterceptorHandler(){
        return new JwtInterceptorHandler();
    }

}
