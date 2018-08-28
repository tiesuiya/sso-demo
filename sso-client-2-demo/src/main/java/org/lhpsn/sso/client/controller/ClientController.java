package org.lhpsn.sso.client.controller;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.lhpsn.sso.client.util.HttpUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("/")
    public String sso(HttpServletRequest request, HttpServletResponse response, String ticket) throws UnsupportedEncodingException {
        // 检查已登录，正常执行
        Boolean isLogin = (Boolean) request.getSession().getAttribute("isLogin");
        if (isLogin != null && isLogin) {
            return "index";
        }

        /*
         * Protected app validates Service Ticket(ST) at CAS server over https
         * 受保护的应用程序通过https验证CAS服务器上的服务票证（ST）
         */
        log.info("CAS Protocol flow 5 客户端收到ST认证请求，进行认证");
        StringBuffer requestService = request.getRequestURL();
        String urlEncoderService = URLEncoder.encode(String.valueOf(requestService), "utf-8");
        // 自己发送请求时ticket也要进行URL编码，Spring MVC发送时不用编码，可能是因为它默认编码了
        String urlEncoderTicket = URLEncoder.encode(String.valueOf(ticket), "utf-8");
        String param = "service=" + urlEncoderService + "&ticket=" + urlEncoderTicket;
        String responseStr = HttpUtils.sendGet("http://www.sso.com:8080/serviceValidate", param);

        /*
         * Set the session cookie and forward the browser back to the application with the service ticket stripped off
         * This service ticket stripped off This optional step prevents the browser address bar from displaying the ST
         * 设置会话cookie并将浏览器转发回应用程序并删除服务票据
         * 此服务票据已剥离此可选步骤可防止浏览器地址栏显示ST
         */
        log.info("CAS Protocol flow 7 客户端收到ST认证相应，进行处理");
        Gson gson = new Gson();
        HashMap map = gson.fromJson(responseStr, HashMap.class);
        String success = (String) map.get("success");
        String userName = (String) map.get("userName");
        // 定义成功消息
        String successMessage = "success";
        if (successMessage.equals(success)) {
            // 设置session
            request.getSession().setAttribute("isLogin", true);
            // 有效期30分钟
            request.getSession().setMaxInactiveInterval(30 * 60);
            request.getSession().setAttribute("userName", userName);
            return "redirect:" + requestService;
        } else {
            log.debug("客户端验证ST失败");
            return "redirect:error";
        }
    }
}
