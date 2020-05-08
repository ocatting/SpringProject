package com.demo.login.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@RestController
public class OrderController {

    @RequestMapping("/order")
    public String order(){
        log.info("下订单");
        return "下订单:";
    }
}
