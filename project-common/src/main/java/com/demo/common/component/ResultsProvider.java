package com.demo.common.component;

/**
 * @Description: Exception 提供固定格式异常
 * @Author Yan XinYu
 **/
public class ResultsProvider {

    public static RuntimeException error(String val){
        String msg = String.format("ParamValidator internal error! Please check the parameters :%s",val);
        return new IllegalArgumentException(msg);
    }

    public static RuntimeException requireError(String val){
        String msg = String.format("The parameter require is not empty! Please check the parameters :%s",val);
        return new IllegalArgumentException(msg);
    }

    public static RuntimeException lengthError(String val,int length){
        String msg = String.format("The parameter length is max/min ! Please check the parameters :%s,%d",val,length);
        return new IllegalArgumentException(msg);
    }

    public static RuntimeException TypeError(String val,Param.type type){
        String msg = String.format("The parameter not %s ! Please check the parameters :%s",type.name(),val);
        return new IllegalArgumentException(msg);
    }

    public static RuntimeException regexError(String val){
        String msg = String.format("The parameter regex is incorrect ! Please check the parameters :%s",val);
        return new IllegalArgumentException(msg);
    }
}
