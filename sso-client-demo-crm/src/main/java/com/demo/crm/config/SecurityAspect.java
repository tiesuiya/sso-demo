package com.demo.crm.config;

import com.demo.crm.annotation.Security;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.lhpsn.sso.common.dto.PermissionDTO;
import org.lhpsn.sso.common.dto.UserDTO;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 客户端Web配置
 *
 * @Author: lihong
 * @Date: 2018/9/4
 * @Description
 */
@Aspect
@Component
public class SecurityAspect {

    @Pointcut("execution(public * com.demo.crm.controller.*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void deBefore(JoinPoint joinPoint) throws Throwable {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpSession session = (HttpSession) requestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
        UserDTO currUser = (UserDTO) session.getAttribute("logonUser");

        // 获取当前注解
        MethodInvocationProceedingJoinPoint methodPoint = (MethodInvocationProceedingJoinPoint) joinPoint;
        Field proxy = methodPoint.getClass().getDeclaredField("methodInvocation");
        proxy.setAccessible(true);
        ReflectiveMethodInvocation j = (ReflectiveMethodInvocation) proxy.get(methodPoint);
        Method method = j.getMethod();
        Security security = method.getAnnotation(Security.class);

        if (security != null) {
            String permission = security.value();
            if (!currUser.getPermissionList().contains(new PermissionDTO(permission))) {
                throw new RuntimeException("权限不足：" + permission);
            }
        }
    }
}  