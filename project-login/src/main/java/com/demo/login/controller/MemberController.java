package com.demo.login.controller;

import com.demo.common.CommonResult;
import com.demo.common.exception.BusinessException;
import com.demo.login.config.properties.JwtProperties;
import com.demo.login.domain.UmsMember;
import com.demo.login.domain.vo.RegisterVo;
import com.demo.login.service.MemberService;
import com.demo.login.util.JwtKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: @Validated 参数校验
 * MethodArgumentNotValidException 这种异常捕捉只会捕捉到 requestBody中的数据
 * @Author Yan XinYu
 **/
@Validated
@RestController
@RequestMapping("/sso")
public class MemberController extends BaseController{

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtKit jwtKit;

    @Autowired
    private JwtProperties jwtProperties;

    @RequestMapping("/h")
    public CommonResult h(@Validated @NotBlank String h){
        return CommonResult.success(h);
    }

    @RequestMapping("/c")
    public CommonResult c(@Validated @RequestBody @NotBlank String c){
        return CommonResult.success(c);
    }

    /**
     * 动态获取手机验证码
     * @param telPhone
     * @return
     * @throws BusinessException
     */
    @PostMapping("/getOtpCode")
    public CommonResult getOtpCode(@RequestParam @NotBlank String telPhone) throws BusinessException {
        String otpCode = memberService.getOtpCode(telPhone);
        return CommonResult.success(otpCode);
    }

    @PostMapping("/register")
    public CommonResult register(@Validated @RequestBody RegisterVo registerVo) throws BusinessException{
        int register = memberService.register(registerVo);
        if (register > 0){
            return CommonResult.success("登录成功");
        }
        return CommonResult.failed();
    }

    /**
     * session 存放在服务端相对更加安全
     * @param username
     * @param password
     * @return
     * @throws BusinessException
     */
    @PostMapping("/login")
    public CommonResult login(@RequestParam @NotBlank String username,
                              @RequestParam @NotBlank String password) throws BusinessException {
        UmsMember umsMember = memberService.login(username, password);
        if (umsMember !=null){
            getHttpSession().setAttribute("member",umsMember);
            return CommonResult.success("登录成功");
        }
        return CommonResult.failed();
    }

    /**
     * jwt(java web token) 登录方式
     * 将用户信息存放在client中，减少服务端资源(需要数据脱敏)
     * 数据泄露后仍然可以使用
     * @param username
     * @param password
     * @return
     * @throws BusinessException
     */
    @PostMapping("/jwtLogin")
    public CommonResult jwtLogin(@RequestParam @NotBlank String username,
                              @RequestParam @NotBlank String password) throws BusinessException {
        UmsMember umsMember = memberService.login(username, password);
        if (umsMember !=null){
            // 将 jwt 登录信息返回
            Map<String,String> map = new HashMap<>();
            String token = jwtKit.generateJwtToken(umsMember);
            map.put("tokenHead",jwtProperties.getTokenHead());
            map.put("token",token);
            return CommonResult.success(map);
        }
        return CommonResult.failed();
    }

    /**
     * 单点登录
     * @return
     */
    @PostMapping("/oauthLogin")
    public CommonResult oauthLogin(){


        return CommonResult.failed();
    }

}
