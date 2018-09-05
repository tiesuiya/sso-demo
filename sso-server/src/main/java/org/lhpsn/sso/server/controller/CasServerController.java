package org.lhpsn.sso.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.lhpsn.sso.common.CasConst;
import org.lhpsn.sso.common.dto.UserDTO;
import org.lhpsn.sso.common.dto.ValidateDTO;
import org.lhpsn.sso.common.util.HttpUtils;
import org.lhpsn.sso.server.bean.LogonInfo;
import org.lhpsn.sso.server.bean.Tgt;
import org.lhpsn.sso.server.dao.LogonInfoRedisDao;
import org.lhpsn.sso.server.service.CasService;
import org.lhpsn.sso.server.service.TgtService;
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
import java.util.List;

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
    private CasService casService;

    @Autowired
    private LogonInfoRedisDao LogonInfoRedisDao;

    @Autowired
    private TgtService tgtService;

    @GetMapping(CasConst.SERVER_LOGIN_PATH)
    public String login(String service, @CookieValue(value = CasConst.CASTGC_COOKIE, required = false) String tgc, ModelMap modelMap, RedirectAttributes redirectAttributes) {
        // service不能为空
        if (StringUtils.isEmpty(service)) {
            modelMap.put("message", "未指定服务地址");
            return "error";
        }

        // 验证TGT，验证成功直接重定向到service
        if (!StringUtils.isEmpty(tgc)) {
            Tgt tgt = tgtService.get(tgc);
            if (tgt != null) {
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

    @PostMapping(CasConst.SERVER_LOGIN_PATH)
    public String login(String userName, String passWord, String service, ModelMap modelMap, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        /*
         * username, password, and login ticket are POSTed in the body
         * 用户名，密码和登录ticket在POST的body中
         */
        log.info("CAS Protocol flow 3 服务端验证用户登录");
        UserDTO userDTO = userService.get(userName, passWord);
        if (userDTO == null || StringUtils.isEmpty(userName)) {
            modelMap.put("error", "用户名或密码错误！");
            modelMap.put("service", service);
            return "login";
        } else {
            /*
             * User is authenticated so create Single-signon (SSO) session CASTGC cookie contains the Ticket Granting Ticket (TGT)
             * The TGT is the session key for the users SSO session
             * 用户已通过身份验证，因此创建单点登录（SSO）会话CASTGC cookie包含票证授予票证（TGT）
             * TGT是用户SSO会话的会话密钥
             */
            log.info("CAS Protocol flow 4 服务端登录成功，生成一系列CAS验证对象");
            // 生成TGC
            String tgc = casService.generateTGC();
            // 保存TGT
            tgtService.save(tgc, userDTO);
            // 设置TGC
            Cookie cookie = new Cookie(CasConst.CASTGC_COOKIE, tgc);
            cookie.setMaxAge(60 * 60);
            cookie.setPath("/");
            response.addCookie(cookie);
            // 生成ST
            String st = casService.generateST(tgc);
            // 重定向至客户端
            redirectAttributes.addAttribute("ticket", st);
            return "redirect:" + service;
        }
    }


    @GetMapping(CasConst.SERVER_LOGOUT_PATH)
    public String logout(@CookieValue(value = CasConst.CASTGC_COOKIE, required = false) String tgc, HttpServletResponse response) {
        // 获取当前用户登录信息
        Tgt tgt = tgtService.get(tgc);
        if (tgt != null) {
            List<LogonInfo> registerInfo = LogonInfoRedisDao.getLogonInfoList(tgt.getUserDTO().getUserName());
            if (registerInfo != null && registerInfo.size() > 0) {
                // 删除session（客户端）
                for (LogonInfo info : registerInfo) {
                    try {
                        HttpUtils.sendHttpRequest(info.getServiceUrl() + CasConst.CLIENT_LOGOUT_PATH, info.getSessionId());
                    } catch (Exception e) {
                        log.debug("请求客户端登出方法出错，{}", e.getLocalizedMessage());
                    }
                }

                // 删除TGC
                Cookie cookie = new Cookie(CasConst.CASTGC_COOKIE, null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);

                // 删除TGT
                tgtService.remove(tgc);
            }
        }
        return "quit";
    }

    @GetMapping(CasConst.SERVER_ST_VALIDATE_PATH)
    @ResponseBody
    public ValidateDTO serviceValidate(String ticket, String service, String sessionId) {
        /*
         * CAS returns an XML document which includes and indication of success, the authenticated subject, and optionally attributes
         * CAS返回一个XML(当然我认为JSON也行)文档，其中包括成功消息，经过身份验证的主体和可选的属性
         */
        log.info("CAS Protocol flow 6 服务端验证ST，返回消息和用户对象");

        ValidateDTO validateDTO = new ValidateDTO();
        validateDTO.setSuccess(false);

        String tgc = casService.getTGCByST(ticket);
        if (!StringUtils.isEmpty(tgc)) {
            Tgt tgt = tgtService.get(tgc);
            if (tgt != null) {
                UserDTO userDTO = tgt.getUserDTO();
                validateDTO.setSuccess(true);
                validateDTO.setUserDTO(userDTO);
                // 注册已登录服务
                LogonInfoRedisDao.save(userDTO.getUserName(), sessionId, service);
            }
        }
        return validateDTO;
    }
}
