package com.ts.core;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface IRabbitDtsSender {

    void send(Object obj);

    void send(Object obj,String id);

}
