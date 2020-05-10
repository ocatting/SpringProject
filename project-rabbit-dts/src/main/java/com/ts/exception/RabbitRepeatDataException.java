package com.ts.exception;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class RabbitRepeatDataException extends Exception{

    public RabbitRepeatDataException(String message) {
        super(message);
    }

    public RabbitRepeatDataException(Throwable throwable) {
        super(throwable);
    }

    public RabbitRepeatDataException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
