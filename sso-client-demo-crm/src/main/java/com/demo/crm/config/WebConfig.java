package com.demo.crm.config;

import org.lhpsn.sso.client.filter.SsoLoginFilter;
import org.lhpsn.sso.client.filter.SsoLogoutFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

/**
 * 客户端Web配置
 *
 * @Author: lihong
 * @Date: 2018/8/27
 * @Description FIXME 客户端配置2（配置过滤器）
 */
@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean ssoLoginFilter() {
        //创建过滤器注册bean
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new SsoLoginFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean ssoLogoutFilter() {
        // 创建过滤器注册bean
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new SsoLogoutFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        return registrationBean;
    }
}
