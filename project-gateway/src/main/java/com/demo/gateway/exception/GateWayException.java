package com.demo.gateway.exception;

import com.demo.common.IErrorCode;
import lombok.Data;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
public class GateWayException extends RuntimeException{

    private long code;

    private String message;

    public GateWayException(IErrorCode iErrorCode) {
        this.code = iErrorCode.getCode();
        this.message = iErrorCode.getMessage();
    }
}
