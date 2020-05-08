package com.demo.oauth.service;

import com.demo.oauth.domain.MemberDetails;
import com.demo.oauth.domain.UmsMember;
import com.demo.oauth.domain.UmsMemberExample;
import com.demo.oauth.mapper.UmsMemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@Service
public class UmsMemberUserDetailService implements UserDetailsService {

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("登录用户名为空");
        }

        UmsMember umsMember = getByUsername(username);
        if(umsMember == null){
            throw new UsernameNotFoundException("Not found any user for username[" + username + "]");
        }
        // 这里默认授权方式
        return new MemberDetails(umsMember);
    }

    /**
     * 方法实现说明:根据用户名获取用户信息
     * @author:smlz
     * @param username:用户名
     * @return: UmsMember 会员对象
     * @exception:
     * @date:2020/1/21 21:34
     */
    public UmsMember getByUsername(String username) {
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsMember> memberList = umsMemberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(memberList)) {
            return memberList.get(0);
        }
        return null;
    }
}
