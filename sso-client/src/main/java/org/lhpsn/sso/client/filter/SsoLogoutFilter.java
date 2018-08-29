package org.lhpsn.sso.client.filter;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.lhpsn.sso.common.CasConst;
import org.lhpsn.sso.common.util.HttpUtils;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * CAS客户端过滤器
 *
 * @Author: lihong
 * @Date: 2018/8/27
 * @Description
 */
@Slf4j
public class SsoLoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();

        // 客户端收到ST验证请求
        String stCheckRequestURI = "/";
        String stCheckRequestParam = "ticket";
        String ticket = request.getParameter(stCheckRequestParam);
        if (stCheckRequestURI.equals(requestURI) && !StringUtils.isEmpty(ticket)) {
            /*
             * Protected app validates Service Ticket(ST) at CAS server over https
             * 受保护的应用程序通过https验证CAS服务器上的服务票证（ST）
             */
            log.info("CAS Protocol flow 5 客户端收到ST认证请求，进行认证");
            StringBuffer requestService = request.getRequestURL();
            String urlEncoderService = URLEncoder.encode(String.valueOf(requestService), "utf-8");
            // 客户端发送请求时ticket也要进行URL编码，Spring MVC发送时不用编码，可能是因为它默认编码了
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
                response.sendRedirect(String.valueOf(requestService));
                return;
            } else {
                log.debug("客户端验证ST失败");
                response.getWriter().write("ST验证错误");
                return;
            }
        }

        // 登录检查
        Boolean isLogin = (Boolean) request.getSession().getAttribute(CasConst.LOGGED_SESSION_NAME);
        if (isLogin != null && isLogin) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            /*
             * Access is unauthenticated so forward to CAS for authentication. "service" query parameter https://app.example.com/ is URL encoded
             * 访问未经身份验证，因此转发到CAS进行身份验证。 “service”查询参数https://app.example.com/是URL编码的
             */
            log.info("CAS Protocol flow 1 客户端检查用户是否登录，没有则重定向到SSO登录");
            StringBuffer requestService = request.getRequestURL();
            String urlEncoderService = URLEncoder.encode(String.valueOf(requestService), "utf-8");
            // TODO url管理
            String redirectUrl = "http://www.sso.com:8080/cas/login?service=" + urlEncoderService;
            response.sendRedirect(redirectUrl);
        }

    }

    @Override
    public void destroy() {

    }
}
