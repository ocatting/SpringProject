package com.demo.login.domain.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
public class RegisterVo {

    @NotBlank
    @Length(max = 11,min = 11,message = "手机号必须是11位")
    @Pattern(regexp = "^1[3|4|5|8][0-9]\\d{8}$",message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "动态校验码不允许为空")
    @Length(max = 6,min = 6,message = "校验码必须是6字符")
    private String otpCode;

    @NotBlank(message = "用户名不允许为空")
    @Size(max = 20,min = 4,message = "用户名长度必须在4-20字符之间")
    private String username;

    @NotBlank
    @Size(max = 20,min = 6,message = "密码小6位，最大20位")
    private String password;

}
