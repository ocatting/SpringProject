package com.demo.gateway.filter;

import com.demo.gateway.properties.GateWayNotAuthUrlProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@Component
@EnableConfigurationProperties(GateWayNotAuthUrlProperties.class)
public class AuthorizationFilter implements GlobalFilter, Ordered {

    private final static int ORDERED = -1;

    @Autowired
    private GateWayNotAuthUrlProperties gateWayNotAuthUrlProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String currentUrl = exchange.getRequest().getURI().getPath();

        /**
         * 不需要认证的地址 跳过 /auth 得跳过
         */
        if(shouldSkip(currentUrl)){
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");


        return chain.filter(exchange);
    }

    private boolean shouldSkip(String currentUrl) {
        //路径匹配器(简介SpringMvc拦截器的匹配器)
        //比如/oauth/** 可以匹配/oauth/token    /oauth/check_token等
        PathMatcher pathMatcher = new AntPathMatcher();
        for(String skipPath:gateWayNotAuthUrlProperties.getShouldSkipUrls()) {
            if(pathMatcher.match(skipPath,currentUrl)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return ORDERED;
    }
}
