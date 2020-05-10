package com.ts.listener;

import org.springframework.amqp.core.Message;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface IRabbitDtsListener {

    /**
     * 最终结果都将会被处理成jsonString
     * @param jsonString
     */
    void process(String jsonString) throws Exception;

    /**
     * 验证通过 true;
     * 出现幂等 false
     * @param message
     * @return
     * @throws Exception
     */
    boolean verification(Message message) throws Exception;
}
