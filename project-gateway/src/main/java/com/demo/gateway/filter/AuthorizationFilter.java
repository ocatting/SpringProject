package com.demo.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.demo.common.ResultCode;
import com.demo.gateway.exception.GateWayException;
import com.demo.gateway.properties.GateWayNotAuthUrlProperties;
import com.demo.gateway.util.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.PublicKey;
import java.util.Map;

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

    /**
     * jwt的公钥,需要网关启动,远程调用认证中心去获取公钥
     */
    private PublicKey publicKey;

    @Override
    public int getOrder() {
        return ORDERED;
    }

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

        if(StringUtils.isEmpty(authHeader)){
            log.warn("认证信息为空");
            throw new GateWayException(ResultCode.AUTHORIZATION_HEADER_IS_EMPTY);
        }

        Claims claims = JwtUtils.validateJwtToken(authHeader,publicKey);

        ServerWebExchange webExchange = wrapHeader(exchange,claims);

        return chain.filter(webExchange);
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

    /**
     * 方法实现说明:把我们从jwt解析出来的用户信息存储到请求中
     * @author:smlz
     * @param serverWebExchange
     * @param claims
     * @return: ServerWebExchange
     * @exception:
     * @date:2020/1/22 12:12
     */
    private ServerWebExchange wrapHeader(ServerWebExchange serverWebExchange,Claims claims) {

        String loginUserInfo = JSON.toJSONString(claims);

        //log.info("jwt的用户信息:{}",loginUserInfo);

        String memberId = claims.get("additionalInfo", Map.class).get("memberId").toString();

        String nickName = claims.get("additionalInfo",Map.class).get("nickName").toString();

        //向headers中放文件，记得build
        ServerHttpRequest request = serverWebExchange.getRequest().mutate()
                .header("username",claims.get("user_name",String.class))
                .header("memberId",memberId)
                .header("nickName",nickName)
                .build();

        //将现在的request 变成 change对象
        return serverWebExchange.mutate().request(request).build();
    }
}
