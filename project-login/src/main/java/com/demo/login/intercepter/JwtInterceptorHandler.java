package com.demo.login.intercepter;

import com.demo.common.CommonResult;
import com.demo.common.exception.BusinessException;
import com.demo.login.config.properties.JwtProperties;
import com.demo.login.util.JwtKit;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * jwt 可用来组合使用用户非敏感信息，将jwt中的数据存放在客户端。
 * 而session存放敏感信息。
 * @Author Yan XinYu
 **/
@Slf4j
public class JwtInterceptorHandler implements HandlerInterceptor {

    @Autowired
    private JwtKit jwtKit;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * request 获取用户信息
     */
    public final static String GLOBAL_JWT_USER_INFO="jwtToken:userMember:Info";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("JwtInterceptorHandler:拦截器");
        String authorization = request.getHeader(jwtProperties.getTokenHeader());
        log.info("authorization:"+authorization);
        //校验token
        if(!StringUtils.isEmpty(authorization)
                && authorization.startsWith(jwtProperties.getTokenHead())){
            String authToken = authorization.substring(jwtProperties.getTokenHead().length());
            try {
                Claims claims = jwtKit.parseJwtToken(authToken);
                if(claims != null){
                    request.setAttribute(GLOBAL_JWT_USER_INFO,claims);
                    return true;
                }
            } catch (BusinessException e) {
                log.error(e.getMessage()+":"+authToken);
            }
        }
        print(response,"您没有权限访问！请先登录。");
        return false;
    }

    protected void print(HttpServletResponse response,String message) throws Exception{
        /**
         * 设置响应头
         */
        response.setHeader("Content-Type","application/json");
        response.setCharacterEncoding("UTF-8");
        String result = new ObjectMapper().writeValueAsString(CommonResult.forbidden(message));
        response.getWriter().print(result);
    }
}
