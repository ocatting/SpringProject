package com.demo.login.exception;

import com.demo.common.CommonResult;
import com.demo.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public CommonResult exceptionHandler(BusinessException ex){
        return CommonResult.failed(ex.getMessage());
    }

    /**
     * JSON方式：校验失败后，会抛出一个MethodArgumentNotValidException (仅处理@RequestBody)
     * 表单方式：校验失败，会抛出一个ConstraintViolationException(仅处理@RequestParam,不可以用Bean接收,返回bindException)
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult exceptionHandler(MethodArgumentNotValidException ex){
        return CommonResult.failed("参数校验异常:"+ex.getBindingResult().getFieldError().getDefaultMessage());
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(IllegalArgumentException.class)
//    public CommonResult exceptionHandler(IllegalArgumentException ex){
//        log.info("参数验证异常:{}",ex.getMessage());
//        return CommonResult.failed("参数异常");
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public CommonResult exceptionHandler(Exception ex){
        log.info("系统故障:{}",ex.getMessage());
        ex.printStackTrace();
        return CommonResult.failed("系统故障");
    }



}
