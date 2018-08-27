package org.lhpsn.sso.client.config;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * CAS客户端过滤器
 *
 * @Author: lihong
 * @Date: 2018/8/27
 * @Description
 */
@Slf4j
public class CasClientFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /*
         * Access is unauthenticated so forward to CAS for authentication. "service" query parameter https://app.example.com/ is URL encoded
         * 访问未经身份验证，因此转发到CAS进行身份验证。 “service”查询参数https://app.example.com/是URL编码的
         */
        log.info("CAS Protocol flow 1 客户端检查用户是否登录，没有则重定向到SSO登录");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String isLogin = (String) request.getSession().getAttribute("isLogin");
        String logged = "true";

        if (!logged.equals(isLogin)) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
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
