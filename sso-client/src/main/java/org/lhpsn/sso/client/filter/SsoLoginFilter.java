package org.lhpsn.sso.client.filter;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.lhpsn.sso.common.CasConst;
import org.lhpsn.sso.common.dto.UserDTO;
import org.lhpsn.sso.common.dto.ValidateDTO;
import org.lhpsn.sso.common.util.HttpUtils;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * SSO单点登录过滤器
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
        HttpSession session = request.getSession();
        String requestURL = request.getRequestURL().toString();
        String requestURI = request.getRequestURI();

        // 客户端发起统一登出请求
        String serverLogoutPath = CasConst.SERVER_LOGOUT_PATH;
        if (requestURI.contains(serverLogoutPath)) {
            // 重定向服务端，进行统一登出
            response.sendRedirect("http://www.sso.com:8080/" + serverLogoutPath);
            return;
        }

        // 客户收到登出请求
        if (requestURI.contains(CasConst.CLIENT_LOGOUT_PATH)) {
            // 注销session
            session.invalidate();
            String message = "客户端注销成功，" + requestURL;
            response.getWriter().print(message);
            return;
        }

        // 客户端收到ST验证请求
        String ticket = request.getParameter(CasConst.SESSION_TICKET);
        if (!StringUtils.isEmpty(ticket)) {
            /*
             * Protected app validates Service Ticket(ST) at CAS server over https
             * 受保护的应用程序通过https验证CAS服务器上的服务票证（ST）
             */
            log.info("CAS Protocol flow 5 客户端收到ST认证请求，进行认证");
            String urlEncoderService = URLEncoder.encode(requestURL, "utf-8");
            // 客户端发送请求时ticket也要进行URL编码，Spring MVC发送时不用编码，可能是因为它默认编码了
            String urlEncoderTicket = URLEncoder.encode(String.valueOf(ticket), "utf-8");
            String sessionId = request.getSession().getId();
            String param = "service=" + urlEncoderService +
                    "&ticket=" + urlEncoderTicket +
                    "&sessionId=" + sessionId;
            String responseStr = HttpUtils.sendGet("http://www.sso.com:8080/" + CasConst.SERVER_ST_VALIDATE_PATH, param);

            /*
             * Set the session cookie and forward the browser back to the application with the service ticket stripped off
             * This service ticket stripped off This optional step prevents the browser address bar from displaying the ST
             * 设置会话cookie并将浏览器转发回应用程序并删除服务票据
             * 此服务票据已剥离此可选步骤可防止浏览器地址栏显示ST
             */
            log.info("CAS Protocol flow 7 客户端收到ST认证相应，进行处理");
            Gson gson = new Gson();
            ValidateDTO validateDTO = gson.fromJson(responseStr, ValidateDTO.class);
            Boolean success = validateDTO.getSuccess();
            UserDTO userDTO = validateDTO.getUserDTO();
            // 定义成功消息
            if (success) {
                // 设置session
                session.setAttribute("isLogin", true);
                // 有效期30分钟
                session.setMaxInactiveInterval(30 * 60);
                session.setAttribute("logonUser", userDTO);
                response.sendRedirect(requestURL);
                return;
            } else {
                log.debug("客户端验证ST失败");
                response.getWriter().write("ST验证错误");
                return;
            }
        }

        // 登录检查
        Boolean isLogin = (Boolean) session.getAttribute(CasConst.LOGGED_SESSION);
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
            String redirectUrl = "http://www.sso.com:8080/" + CasConst.SERVER_LOGIN_PATH + "?service=" + urlEncoderService;
            response.sendRedirect(redirectUrl);
        }
    }

    @Override
    public void destroy() {

    }
}
