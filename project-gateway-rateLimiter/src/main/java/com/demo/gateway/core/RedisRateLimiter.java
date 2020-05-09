package com.demo.gateway.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public final class RedisRateLimiter {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * RedisScript 脚本
     * 流程
     * 1. redis.call('incr',key1);这个参数自增1；
     * 若==1则设置过期时间为 Redis Expire 命令用于设置 key 的过期时间，key 过期后将不再可用。单位以秒计。
     * 否则判断是否超过
     */
    private static final String LUA_SECOND_SCRIPT = " local current; "
            + " current = redis.call('incr',KEYS[1]); "
            + " if tonumber(current) == 1 then "
            + " 	redis.call('expire',KEYS[1],ARGV[1]); "
            + "     return 1; "
            + " else"
            + " 	if tonumber(current) <= tonumber(ARGV[2]) then "
            + "     	return 1; "
            + "		else "
            + "			return -1; "
            + "     end "
            + " end ";
    private static final String LUA_PERIOD_SCRIPT =   " local currentSectionCount;"
            + " local previousSectionCount;"
            + " local totalCountInPeriod;"
            + " currentSectionCount = redis.call('zcount', KEYS[2], '-inf', '+inf');"
            + " previousSectionCount = redis.call('zcount', KEYS[1], ARGV[3], '+inf');"
            + " totalCountInPeriod = tonumber(currentSectionCount)+tonumber(previousSectionCount);"
            + " if totalCountInPeriod < tonumber(ARGV[5]) then "
            + " 	redis.call('zadd',KEYS[2],ARGV[1],ARGV[2]);"
            + "		if tonumber(currentSectionCount) == 0 then "
            + "			redis.call('expire',KEYS[2],ARGV[4]); "
            + "		end "
            + "     return 1"
            + "	else "
            + " 	return -1"
            + " end ";

    /**
     * 统一时间格式，在redis中通过lua脚本相加
     */
    private static final int PERIOD_SECOND_TTL = 10;
    private static final int PERIOD_MINUTE_TTL = 2 * 60 + 10;
    private static final int PERIOD_HOUR_TTL = 2 * 3600 + 10;
    private static final int PERIOD_DAY_TTL = 2 * 3600 * 24 + 10;

    private static final long MICROSECONDS_IN_MINUTE = 60 * 1000000;
    private static final long MICROSECONDS_IN_HOUR = 3600 * 1000000;
    private static final long MICROSECONDS_IN_DAY = 24 * 3600 * 1000000;

    /**
     * 请求
     * @param rateLimiterKey
     * @param permits
     * @return
     */
    public boolean acquire(String rateLimiterKey, int permits , TimeUnit timeUnit){
        boolean rtv = false;
        if(timeUnit == TimeUnit.SECONDS){
            DefaultRedisScript defaultRedisScript = new DefaultRedisScript();
            defaultRedisScript.setResultType(String.class);
            defaultRedisScript.setScriptText(LUA_SECOND_SCRIPT);

            List<String> keys = new ArrayList<>(1);
            keys.add(rateLimiterKey);
            List<Integer> args = new ArrayList<>(2);
            args.add(getExpire(timeUnit));//expire
            args.add(permits);
            Integer val = (Integer) redisTemplate.execute(defaultRedisScript, keys, args);
            rtv = (val > 0);
        } else if(timeUnit == TimeUnit.MINUTES || timeUnit == TimeUnit.HOURS || timeUnit == TimeUnit.DAYS){
            rtv = doPeriod(rateLimiterKey,permits,timeUnit);
        }
        return rtv;
    }

    /**
     * 当前时间毫秒数
     * @return
     */
    public Long currentSeconds(){
        return System.currentTimeMillis()/1000;
    }

    private boolean doPeriod(String keyPrefix, int permitsPerUnit,TimeUnit timeUnit) {
        String[] keyNames = getKeyNames(keyPrefix,timeUnit);
        long currentTimeInMicroSecond = currentSeconds();
        String previousSectionBeginScore = String.valueOf((currentTimeInMicroSecond - getPeriodMicrosecond(timeUnit)));
        String expires =String.valueOf(getExpire(timeUnit));
        String currentTimeInMicroSecondStr = String.valueOf(currentTimeInMicroSecond);
        List<String> keys = new ArrayList<String>(2);
        keys.add(keyNames[0]);
        keys.add(keyNames[1]);
        List<String> args = new ArrayList<>(5);
        args.add(currentTimeInMicroSecondStr);
        args.add(currentTimeInMicroSecondStr);
        args.add(previousSectionBeginScore);
        args.add(expires);
        args.add(String.valueOf(permitsPerUnit));

        DefaultRedisScript defaultRedisScript = new DefaultRedisScript();
        defaultRedisScript.setResultType(String.class);
        defaultRedisScript.setScriptText(LUA_PERIOD_SCRIPT);
        Integer val = (Integer) redisTemplate.execute(defaultRedisScript, keys, args);
        return (val > 0);
    }

    private String[] getKeyNames(String keyPrefix,TimeUnit timeUnit) {
        String[] keyNames = null;
        long time = currentSeconds();
        if (timeUnit == TimeUnit.MINUTES) {
            long index = time / 60;
            String keyName1 = keyPrefix + ":" + (index - 1);
            String keyName2 = keyPrefix + ":" + index;
            keyNames = new String[] { keyName1, keyName2 };
        } else if (timeUnit == TimeUnit.HOURS) {
            long index = time / 3600;
            String keyName1 = keyPrefix + ":" + (index - 1);
            String keyName2 = keyPrefix + ":" + index;
            keyNames = new String[] { keyName1, keyName2 };
        } else if (timeUnit == TimeUnit.DAYS) {
            long index = time / (3600 * 24);
            String keyName1 = keyPrefix + ":" + (index - 1);
            String keyName2 = keyPrefix + ":" + index;
            keyNames = new String[] { keyName1, keyName2 };
        } else {
            throw new IllegalArgumentException("Don't support this TimeUnit: " + timeUnit);
        }
        return keyNames;
    }

    private int getExpire(TimeUnit timeUnit) {
        int expire = 0;
        if (timeUnit == TimeUnit.SECONDS) {
            expire = PERIOD_SECOND_TTL;
        } else if (timeUnit == TimeUnit.MINUTES) {
            expire = PERIOD_MINUTE_TTL;
        } else if (timeUnit == TimeUnit.HOURS) {
            expire = PERIOD_HOUR_TTL;
        } else if (timeUnit == TimeUnit.DAYS) {
            expire = PERIOD_DAY_TTL;
        } else {
            throw new IllegalArgumentException("Don't support this TimeUnit: " + timeUnit);
        }
        return expire;
    }

    private long getPeriodMicrosecond(TimeUnit timeUnit) {
        if (timeUnit == TimeUnit.MINUTES) {
            return MICROSECONDS_IN_MINUTE;
        } else if (timeUnit == TimeUnit.HOURS) {
            return MICROSECONDS_IN_HOUR;
        } else if (timeUnit == TimeUnit.DAYS) {
            return MICROSECONDS_IN_DAY;
        } else {
            throw new IllegalArgumentException("Don't support this TimeUnit: " + timeUnit);
        }
    }

}
