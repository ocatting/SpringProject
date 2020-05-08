package com.demo.login.service;

import com.demo.common.TokenInfo;
import com.demo.common.exception.BusinessException;
import com.demo.login.domain.UmsMember;
import com.demo.login.domain.vo.RegisterVo;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface MemberService {

    /**
     * 获取动态验证码
     * @param telPhone
     * @return
     * @throws BusinessException
     */
    String getOtpCode(String telPhone) throws BusinessException;

    /**
     * 注册
     * @param registerVo
     * @return
     * @throws BusinessException
     */
    int register(RegisterVo registerVo) throws BusinessException;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     * @throws BusinessException
     */
    UmsMember login(String username, String password)throws BusinessException;

    /**
     * 单点登录方式
     * @param username
     * @param password
     * @return
     * @throws BusinessException
     */
    TokenInfo oauthLogin(String username, String password)throws BusinessException;
}
