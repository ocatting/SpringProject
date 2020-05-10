package com.ts.exception;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class RabbitDtsException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public RabbitDtsException(String message) {
        super(message);
    }

    public RabbitDtsException(Throwable throwable) {
        super(throwable);
    }

    public RabbitDtsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
