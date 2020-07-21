package com.demo.common.component;

import com.alibaba.fastjson.JSON;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class BaseHttpServlet {

    protected HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected HttpServletResponse getResponse(){
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * GET请求从url中获取数据，POST请求从body中获取数据
     * @param request
     * @param body
     * @return
     */
    protected Map<String,Object> toMap(HttpServletRequest request,String body){
        String method = request.getMethod().toUpperCase();
        if(method.equals("POST")){
            String contentType = request.getHeader("Content-Type");
            if(contentType.equals("application/json")){
                return JSON.parseObject(body);
            }
        }else if(method.equals("GET")) {
            Enumeration<String> enumeration = request.getParameterNames();
            Map<String,Object> map = new HashMap<>();
            while (enumeration.hasMoreElements()){
                String key = enumeration.nextElement();
                String val = request.getParameter(key);
                map.put(key,val);
            }
            return map;
        }
       return null;
    }

    /**
     * 读取Body信息
     * @param request
     * @return
     */
    protected String getRequestBody(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = request.getReader();
            String temp;
            while ((temp = reader.readLine()) !=null){
                sb.append(temp);
            }
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
//            throw new RuntimeException(e.getMessage());
        }
        return sb.toString();
    }

    /**
     * 设置错误响应信息
     */
    protected void setResponse(){
        HttpServletResponse response = getResponse();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json");
        response.setContentType("application/json;charset=UTF-8");
    }
}
