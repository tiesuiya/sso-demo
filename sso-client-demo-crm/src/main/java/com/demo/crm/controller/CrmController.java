package com.demo.crm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.Enumeration;

/**
 * 安全数据测试控制器
 *
 * @Author: lihong
 * @Date: 2018/8/28
 * @Description
 */
@Slf4j
@Controller
public class CrmController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

}
