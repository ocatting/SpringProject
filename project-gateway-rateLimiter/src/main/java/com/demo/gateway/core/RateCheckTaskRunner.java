package com.demo.gateway.core;

import com.demo.gateway.properties.LimiterProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@RequiredArgsConstructor
public final class RateCheckTaskRunner {

    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final LimiterProperties limiterProperties;
    private final RedisRateLimiter redisRateLimiter;

    public boolean checkRun(String rateLimiterKey, TimeUnit timeUnit, int permits) {
        CheckTask checkTask = new CheckTask(rateLimiterKey, timeUnit, permits);
        Future<Boolean> checkResult = executorService.submit(checkTask);
        boolean retVal = false;
        try {
            // 在指定的毫秒内获取数据;
            retVal = checkResult.get(limiterProperties.getCheckActionTimeout(), TimeUnit.MILLISECONDS);
        }
        catch(Exception e) {
            log.error("redis limiter acquire checkResult exception:{}",e.getMessage());
        }
        return retVal;
    }


    class CheckTask implements Callable<Boolean>{
        private String rateLimiterKey;
        private TimeUnit timeUnit;
        private int permits;
        CheckTask(String rateLimiterKey, TimeUnit timeUnit, int permits) {
            this.rateLimiterKey = rateLimiterKey;
            this.timeUnit = timeUnit;
            this.permits = permits;
        }
        public Boolean call() {
            return redisRateLimiter.acquire(rateLimiterKey, permits,timeUnit);
        }
    }

}
