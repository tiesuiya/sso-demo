package org.lhpsn.ssoserverdemo.controller;

import org.lhpsn.ssoserverdemo.User;
import org.lhpsn.ssoserverdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * cas服务端控制器
 *
 * @Author: lihong
 * @Date: 2018/8/24
 * @Description
 */
@Controller
public class CasServerController {

    @Autowired
    private UserService userService;

    @GetMapping("/cas/login")
    public String login(String service, ModelMap modelMap) {
        modelMap.put("service", service);
        return "login";
    }

    @PostMapping("/cas/login")
    public String login(String userName, String passWord) {
        User user = userService.get(userName, passWord);
        if (user != null) {
            // User is authenticated so create single-signon (SSO) session
            // CASTGC cookie contains the Ticket Granting Ticket (TGT)
            // The TGT is the session key for the users SSO session
            // 用户已通过身份验证，因此创建单点登录（SSO）会话
            // CASTGC cookie包含票证授予票证（TGT）
            // TGT是用户SSO会话的会话密钥
        } else {

        }
        return "";
    }
}
