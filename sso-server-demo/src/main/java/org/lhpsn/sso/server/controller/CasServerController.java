package org.lhpsn.sso.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.lhpsn.sso.server.User;
import org.lhpsn.sso.server.dao.TgtRedisDao;
import org.lhpsn.sso.server.service.CasService;
import org.lhpsn.sso.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * cas服务端控制器
 *
 * @Author: lihong
 * @Date: 2018/8/24
 * @Description
 */
@Slf4j
@Controller
public class CasServerController {

    @Autowired
    private UserService userService;

    @Autowired
    private TgtRedisDao tgtRedisDao;

    @Autowired
    private CasService casService;

    @GetMapping("/cas/login")
    public String login(String service, ModelMap modelMap) {
        /*
         * User does not have an SSO session so present login form
         * 用户没有SSO会话，因此提供登录表单
         */
        log.info("CAS Protocol flow 2 服务端收到登录GET请求，跳转至登录页面");
        modelMap.put("service", service);
        return "login";
    }

    @PostMapping("/cas/login")
    public String login(String userName, String passWord, String service, ModelMap modelMap) {
        /*
         * username, password, and login ticket are POSTed in the body
         * 用户名，密码和登录ticket在POST的body中
         */
        log.info("CAS Protocol flow 3 服务端验证用户登录");
        User user = userService.get(userName, passWord);
        if (user == null || StringUtils.isEmpty(userName)) {
            modelMap.put("error", "用户名或密码错误！");
            modelMap.put("service", service);
            return "login";
        } else {
            /*
             * User is authenticated so create single-signon (SSO) session
             * CASTGC cookie contains the Ticket Granting Ticket (TGT)
             * The TGT is the session key for the users SSO session
             * 用户已通过身份验证，因此创建单点登录（SSO）会话
             * CASTGC cookie包含票证授予票证（TGT）
             * TGT是用户SSO会话的会话密钥
             */
            log.info("CAS Protocol flow 4 服务端登录成功，生成一系列CAS验证对象");
            // 生成TGC
            String tgc = casService.generateTGC();
            // 保存TGT
            tgtRedisDao.add(tgc, user);
            // 生成ST
            String st = casService.generateST(tgc);
            // 重定向至客户端
            modelMap.put("ticket", st);
            return "redirect:" + service;
        }
    }
}
