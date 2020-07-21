package com.demo.login.controller;

import com.demo.common.component.Param;
import com.demo.common.component.ParamValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@RestController
public class OrderController {

    @RequestMapping("/order")
    @ParamValidator(parameters = {
            @Param(name = "id", type = Param.type.LONG, require = true),
            @Param(name = "mobile", type = Param.type.MOBILE, minLength = 11),
            @Param(name = "email", type = Param.type.EMAIL, maxLength = 50) })
    public String order(@RequestBody(required = false) Map<String ,Object> map){
        log.info("下订单:{}",map);
        map.forEach((k,y)->log.info("map:{}:{}",k,y));
        return "下订单:";
    }

    /**
     * @RequestParam(required = false) Map map 可从GET 上获取参数
     * @RequestBody(required = false) Map map 可从body中获取数据
     * @param map
     * @return
     */
    @RequestMapping("/orderTest")
    @ParamValidator()
    public String orderTest(@RequestBody(required = false) Map map){
        log.info("下订单test");
        map.forEach((k,y)->log.info("map:{}:{}",k,y));
        return "下订单:";
    }
}
