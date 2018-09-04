package com.demo.crm.controller;

import com.demo.crm.annotation.Security;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 业务测试控制器
 *
 * @Author: lihong
 * @Date: 2018/8/28
 * @Description
 */
@Slf4j
@RestController
public class BizzController {

    @Security("添加订单")
    @GetMapping("/addOrder")
    public String addOrder() {
        return "添加订单成功";
    }

    @Security("删除订单")
    @GetMapping("/delOrder")
    public String delOrder() {
        return "删除订单成功";
    }

    @Security("转账")
    @GetMapping("/transfer")
    public String transfer() {
        return "转账成功";
    }

    @Security("提款")
    @GetMapping("/withdrawal")
    public String withdrawal() {
        return "提款成功";
    }

    @ExceptionHandler(Exception.class)
    public String handleMissingServletRequestParameterException(Exception e) {
        return e.getMessage();
    }
}
