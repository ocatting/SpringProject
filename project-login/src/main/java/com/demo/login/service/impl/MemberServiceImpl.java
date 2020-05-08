package com.demo.login.service.impl;

import com.demo.common.exception.BusinessException;
import com.demo.login.config.properties.RedisKeyPrefixConfig;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
}
