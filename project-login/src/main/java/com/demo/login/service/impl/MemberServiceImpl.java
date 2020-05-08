package com.demo.login.service.impl;

import com.demo.common.TokenInfo;
import com.demo.common.exception.BusinessException;
import com.demo.login.config.properties.RedisKeyPrefixConfig;
import com.demo.login.constant.MDA;
import com.demo.login.domain.UmsAdminExample;
import com.demo.login.domain.UmsMember;
import com.demo.login.domain.UmsMemberExample;
import com.demo.login.domain.vo.RegisterVo;
import com.demo.login.mapper.UmsAdminMapper;
import com.demo.login.mapper.UmsMemberMapper;
import com.demo.login.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisKeyPrefixConfig redisKeyPrefixConfig;

    @Override
    public String getOtpCode(String telPhone) throws BusinessException {
        //1.查询是否已注册
        UmsMemberExample umsMemberExample = new UmsMemberExample();
        umsMemberExample.createCriteria().andPhoneEqualTo(telPhone);
        List<UmsMember> umsMembers = umsMemberMapper.selectByExample(umsMemberExample);
        if(!CollectionUtils.isEmpty(umsMembers)){
            throw new BusinessException("用户已经注册");
        }
        //2.校验是否失效
        String redisKey = redisKeyPrefixConfig.getPrefix().getOtpCode() + telPhone;
        long expire = redisKeyPrefixConfig.getExpire().getOtpCode();
        if(redisTemplate.hasKey(redisKey)){
            throw new BusinessException("请"+expire+"s后再试");
        }
        //随机数
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <6 ; i++) {
            sb.append(random.nextInt(10));
        }
        log.info("otpCode:{}",sb.toString());

        redisTemplate.opsForValue().set(redisKey,sb.toString(),expire, TimeUnit.SECONDS);

        return sb.toString();
    }

    @Override
    public int register(RegisterVo registerVo) throws BusinessException {
        String redisKey = redisKeyPrefixConfig.getPrefix().getOtpCode() + registerVo.getPhone();
        String otpCode = (String)redisTemplate.opsForValue().get(redisKey);
        if(StringUtils.isEmpty(otpCode) || !otpCode.equals(registerVo.getOtpCode())){
            throw new BusinessException("动态校验码不正确");
        }
        UmsMember umsMember = new UmsMember();
        umsMember.setStatus(1);
        umsMember.setMemberLevelId(4L);
        BeanUtils.copyProperties(registerVo,umsMember);
        umsMember.setPassword(passwordEncoder.encode(registerVo.getPassword()));

        return umsMemberMapper.insertSelective(umsMember);
    }

    @Override
    public UmsMember login(String username, String password) throws BusinessException {
        UmsMemberExample memberExample = new UmsMemberExample();
        memberExample.createCriteria().andUsernameEqualTo(username).andStatusEqualTo(1);
        List<UmsMember> result = umsMemberMapper.selectByExample(memberExample);
        if(CollectionUtils.isEmpty(result)){
            throw new BusinessException("用户名或密码不正确!");
        }
        if (result.size() > 1) {
            throw new BusinessException("用户名被多次注册!");
        }
        UmsMember umsMember = result.get(0);
        if(!passwordEncoder.matches(password,umsMember.getPassword())){
            throw new BusinessException("用户名或密码不正确");
        }
        log.info("login:{}",umsMember);
        return umsMember;
    }

    @Override
    public TokenInfo oauthLogin(String username, String password) throws BusinessException {
        //封装oauth请求头
        HttpEntity<MultiValueMap<String, String>> httpEntity = wrapOauthTokenRequest(username, password);

        //请求认证信息
        ResponseEntity<TokenInfo> response;
        try{
            /**
             * 该方法中的URL为微服务请求格式，若要正确请求请填写正确的地址与引入注册中心与ribbon负载均衡服务
             */
            response = restTemplate.exchange(MDA.OAUTH_LOGIN_URL, HttpMethod.POST, httpEntity, TokenInfo.class);
            TokenInfo tokenInfo = response.getBody();
            log.info("用户 {} 登录成功信息:{}",username,tokenInfo);
            return tokenInfo;
        }catch (Exception e){
            log.error("根据用户名:{}登陆异常:{}",username,e.getMessage());
        }
        //返回认证信息
        return null;
    }

    /**
     * 封装oauth请求参数
     * @param username
     * @param password
     * @return
     */
    private HttpEntity<MultiValueMap<String, String>> wrapOauthTokenRequest (String username, String password){
        //oauth 请求头部信息
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(MDA.CLIENT_ID,MDA.CLIENT_SECRET);

        //消息体
        MultiValueMap<String, String> reqParams = new LinkedMultiValueMap<>();
        reqParams.add(MDA.USER_NAME,username);
        reqParams.add(MDA.PASS,password);
        reqParams.add(MDA.GRANT_TYPE,MDA.PASS);
        reqParams.add(MDA.SCOPE,MDA.SCOPE_AUTH);
        //封装请求参数
        return new HttpEntity<>(reqParams, httpHeaders);
    }
}
