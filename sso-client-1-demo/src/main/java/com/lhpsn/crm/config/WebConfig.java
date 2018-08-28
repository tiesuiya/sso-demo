package com.lhpsn.crm.config;

import org.lhpsn.sso.client.config.CasClientFilter;
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
 * @Description
 */
@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean casClientFilter() {
        //创建 过滤器注册bean
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new CasClientFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        return registrationBean;

        // 附加参数
        // registrationBean.addInitParameter("targetFilterLifecycle", "true");
        // registrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico");
    }
}
