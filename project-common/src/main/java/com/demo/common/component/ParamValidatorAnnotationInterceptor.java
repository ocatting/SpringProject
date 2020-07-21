package com.demo.common.component;

import com.alibaba.fastjson.JSON;
import com.demo.common.component.utils.RegexUtils;
import com.demo.common.component.utils.TypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: AOP方法拦截
 * @Author Yan XinYu
 **/
@Slf4j
public class ParamValidatorAnnotationInterceptor extends BaseHttpServlet
        implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try{
            //处理 参数
            readParamValidator(invocation);
            //处理业务
            return invocation.proceed();
        }catch (IllegalArgumentException e){
            setResponse();
            return JSON.toJSONString(new ResultMsg(400,e.getMessage(),"Parameter validation failed"));
        }
    }

    /**
     * 读取参数编辑器
     * @param invocation
     */
    private void readParamValidator(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        ParamValidator pv = method.isAnnotationPresent(ParamValidator.class)
                ? method.getAnnotation(ParamValidator.class): null;
        if(pv == null){
            return;
        }
        Param[] parameters = pv.parameters();
//        log.info("方法参数:param:{}",parameters);
        HttpServletRequest request = getRequest();
        String requestBody = getRequestBody(request);
        Map<String, Object> map = toMap(request, requestBody);
        for (Param param :pv.parameters()) {
            resolveParam(param,(String) map.get(param.name()));
        }
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("id","as");
        System.out.println((String) map.get("aa"));
    }

    /**
     * 解析与验证 @Param条件
     * @param param
     * @param value
     */
    private void resolveParam(Param param,String value) {
        String name = param.name();
        // 判断参数是否要求必填，并判断参数是否为空
        if ((param.require() || param.minLength() != -1 || param.maxLength() != -1)
                && StringUtils.isEmpty(value)) {
            throw ResultsProvider.requireError(name);
        }
        // 判断参数是否要验证最小长度，并判断参数是否满足最小长度限制
        if (param.minLength() != -1 && value.length() < param.minLength()) {
            throw ResultsProvider.lengthError(name,value.length());
        }
        // 判断参数是否要验证最大长度，并判断参数是否满足最大长度限制
        if (param.maxLength() != -1 && value.length() > param.maxLength()) {
            throw ResultsProvider.lengthError(name,value.length());
        }
        //验证参数类型
        checkType(param,value);
    }

    public void checkType(Param param,String value) {
        String name = param.name();
        switch (param.type()){
            case INTEGER:
                if(!TypeUtils.isInt(value)){
                    throw ResultsProvider.TypeError(name,Param.type.INTEGER);
                }
            case BOOLEAN:
                if(!TypeUtils.isBoolean(value)){
                    throw ResultsProvider.TypeError(name,Param.type.BOOLEAN);
                }
            case DATE:
                if(!TypeUtils.isDate(value)){
                    throw ResultsProvider.TypeError(name,Param.type.DATE);
                }
            case DOUBLE:
                if(!TypeUtils.isDouble(value)){
                    throw ResultsProvider.TypeError(name,Param.type.DOUBLE);
                }
            case FLOAT:
                if(!TypeUtils.isFloat(value)){
                    throw ResultsProvider.TypeError(name, Param.type.FLOAT);
                }
            case LONG:
                if(!TypeUtils.isLong(value)){
                    throw ResultsProvider.TypeError(name, Param.type.LONG);
                }
            case EMAIL:
                if(!RegexUtils.checkEmail(value, 50)){
                    throw ResultsProvider.TypeError(name, Param.type.EMAIL);
                }
            case MOBILE:
                if(!RegexUtils.checkMobile(value)){
                    throw ResultsProvider.TypeError(name, Param.type.MOBILE);
                }
            default:
        }
        //验证正则表达式
        if(!StringUtils.isEmpty(param.regex()) && !value.matches(param.regex())){
            throw ResultsProvider.regexError(name);
        }
    }



    /**
     * 返回信息
     */
    class ResultMsg{
        public int code;
        public String data;
        public String msg;

        ResultMsg(Integer code,String data,String msg){
            this.code = code;
            this.data = data;
            this.msg = msg;
        }
    }
}
