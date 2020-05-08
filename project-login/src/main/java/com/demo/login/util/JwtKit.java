package com.demo.login.util;

import com.demo.common.exception.BusinessException;
import com.demo.login.config.properties.JwtProperties;
import com.demo.login.domain.UmsMember;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtKit {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 创建jwtToken
     * @param member
     * @return
     */
    public String generateJwtToken(UmsMember member){
        Map<String,Object> claims = new HashMap<>();

        claims.put("sub",member.getUsername());
        claims.put("createdate",new Date());
        claims.put("id",member.getId());
        claims.put("memberLevelId",member.getMemberLevelId());

        return Jwts.builder()
                //.addClaims(claims) 上课异常原因,改成下面的方式
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()*1000))
                .signWith(SignatureAlgorithm.HS256,jwtProperties.getSecret())
                .compact();
    }


    /**
     * 解析jwt
     * @param jwtToken
     * @return
     * @throws BusinessException
     */
    public Claims parseJwtToken(String jwtToken) throws BusinessException {
        Claims claims = null;
        try {
            claims=Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(jwtToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new BusinessException("JWT验证失败:token已经过期");
        } catch (UnsupportedJwtException e) {
            throw new BusinessException("JWT验证失败:token格式不支持");
        } catch (MalformedJwtException e) {
            throw new BusinessException("JWT验证失败:无效的token");
        } catch (SignatureException e) {
            throw new BusinessException("JWT验证失败:无效的token");
        } catch (IllegalArgumentException e) {
            throw new BusinessException("JWT验证失败:无效的token");
        }
        return claims;
    }

}
