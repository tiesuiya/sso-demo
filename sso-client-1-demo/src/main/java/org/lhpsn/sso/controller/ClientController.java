package org.lhpsn.sso.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 客户端控制器
 *
 * @Author: lihong
 * @Date: 2018/8/27
 * @Description
 */
@Slf4j
@Controller
public class ClientController {

    @GetMapping("/cas/")
    public String sso() {
        /*
         * Protected app validates Service Ticket(ST) at CAS server over https
         * 受保护的应用程序通过https验证CAS服务器上的服务票证（ST）
         */

        return null;
    }
}
