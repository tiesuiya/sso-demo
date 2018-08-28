package org.lhpsn.sso.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.lhpsn.sso.server.bean.User;
import org.lhpsn.sso.server.dao.ServiceUrlRedisDao;
import org.lhpsn.sso.server.dao.TgtRedisDao;
import org.lhpsn.sso.server.service.CasService;
import org.lhpsn.sso.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.lhpsn.sso.server.util.CasConfig.CASTGC_COOKIE_NAME;

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
    private ServiceUrlRedisDao clientRedisDao;

    @Autowired
    private CasService casService;

    @GetMapping("/cas/login")
    public String login(String service, @CookieValue(value = CASTGC_COOKIE_NAME, required = false) String tgc, ModelMap modelMap, RedirectAttributes redirectAttributes) {
        // service不能为空
        if (StringUtils.isEmpty(service)) {
            modelMap.put("message", "未指定服务地址");
            return "error";
        }

        // 验证TGT，验证成功直接重定向到service
        if (tgc != null) {
            User user = casService.getUserByTGC(tgc);
            if (user != null) {
                // 生成ST
                String st = casService.generateST(tgc);
                // 重定向至客户端
                redirectAttributes.addAttribute("ticket", st);
                return "redirect:" + service;
            }
        }

        /*
         * User does not have an SSO session so present login form
         * 用户没有SSO会话，因此提供登录表单
         */
        log.info("CAS Protocol flow 2 服务端收到登录GET请求，跳转至登录页面");
        modelMap.put("service", service);
        return "login";
    }

    @PostMapping("/cas/login")
    public String login(String userName, String passWord, String service, ModelMap modelMap, RedirectAttributes redirectAttributes, HttpServletResponse response) {
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
            // 设置TGC
            Cookie cookie = new Cookie(CASTGC_COOKIE_NAME, tgc);
            cookie.setMaxAge(60 * 60);
            cookie.setPath("/");
            response.addCookie(cookie);
            // 注册已登录服务，为单点登出做铺垫
            clientRedisDao.add(service);
            // 重定向至客户端
            redirectAttributes.addAttribute("ticket", st);
            return "redirect:" + service;
        }
    }


    @GetMapping("/cas/logout")
    public void logout(String service, @CookieValue(value = CASTGC_COOKIE_NAME, required = false) String tgc, HttpServletResponse response) {
        casService.logout(service, tgc, response);
    }

    @GetMapping("/serviceValidate")
    @ResponseBody
    public Map<String, String> serviceValidate(String ticket) {
        /**
         * CAS returns an XML document which includes and indication of success, the authenticated subject, and optionally attributes
         * CAS返回一个XML(当然我认为JSON也行)文档，其中包括成功消息，经过身份验证的主体和可选的属性
         */
        log.info("CAS Protocol flow 6 服务端验证ST，返回消息和用户对象");
        User user = casService.getUserByST(ticket);

        Map<String, String> responseBody = new HashMap<>();
        if (user != null) {
            responseBody.put("success", "success");
            responseBody.put("userName", user.getUserName());
        } else {
            responseBody.put("success", "fail");
        }
        return responseBody;
    }
}
