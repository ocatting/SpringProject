package com.demo.gateway.ratelimiter;

import com.demo.gateway.core.RateCheckTaskRunner;
import com.demo.gateway.properties.LimiterProperties;
import com.demo.gateway.properties.LimitingPolicy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@Component

public class GatewayRateLimiterFilter implements GlobalFilter, Ordered {

    private static final int ORDER = 100;

    @Autowired
    private LimiterProperties limiterProperties;

    @Autowired
    private RateCheckTaskRunner rateCheckTaskRunner;

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String currentUrl = exchange.getRequest().getURI().getPath();

        // 匹配路径检测是否需要路径限制
        LimitingPolicy limitingPolicy = matcher(currentUrl);
        if( limitingPolicy != null ){
            String base = evel(exchange);
            String redisRateLimiterKey = String.format("%s:%s:%s",limiterProperties.getRedisKeyPrefix(),limitingPolicy.getMatchUrl(),base);
            TimeUnit timeUnit = TimeUnit.valueOf(limitingPolicy.getTimeUnit());
            // key , 时间单位，次数
            boolean b = rateCheckTaskRunner.checkRun(redisRateLimiterKey, timeUnit, limitingPolicy.getPermits());
            if(!b){
                throw new RuntimeException("Access denied because of exceeding access rate!");
            }
        }
        return chain.filter(exchange);
    }

    /**
     * 匹配路径
     * @param currentUrl
     * @return
     */
    private LimitingPolicy matcher(String currentUrl){
        PathMatcher pathMatcher = new AntPathMatcher();
        Map<String, LimitingPolicy> policyMap = limiterProperties.getPolicyMap();
        Optional<LimitingPolicy> target = policyMap.values().stream().filter(
                                            p -> pathMatcher.match(p.getMatchUrl(), currentUrl)).findFirst();
        if(target.isPresent()) {
            return target.get();
        }
        return null;
    }

    /**
     * 解析备用字段
     * @param exchange
     * @return
     */
    private String evel(ServerWebExchange exchange){
        String result = "#BASE";
        return result;
    }


}
