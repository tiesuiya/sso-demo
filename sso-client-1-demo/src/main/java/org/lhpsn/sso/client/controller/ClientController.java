package org.lhpsn.sso.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.lhpsn.sso.client.util.HttpUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 客户端控制器
 *
 * @Author: lihong
 * @Date: 2018/8/27
 * @Description
 */
@Slf4j
@RestController
public class ClientController {

    @GetMapping("/")
    public String sso(HttpServletRequest request, String ticket) throws UnsupportedEncodingException {
        /*
         * Protected app validates Service Ticket(ST) at CAS server over https
         * 受保护的应用程序通过https验证CAS服务器上的服务票证（ST）
         */
        log.info("CAS Protocol flow 5 客户端收到ST认证请求，进行认证");
        StringBuffer requestService = request.getRequestURL();
        String urlEncoderService = URLEncoder.encode(String.valueOf(requestService), "utf-8");
        String param = "service=" + urlEncoderService + "&ticket=" + ticket;
        String responseStr = HttpUtils.sendGet("http://www.sso.com:8080/serviceValidate", param);
        if ("SUCCESS".equals(responseStr)) {
            log.info("验证服务器成功，设置session");
        }
        return responseStr;
    }
}
