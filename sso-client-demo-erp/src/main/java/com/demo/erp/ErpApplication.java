package com.demo.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 模拟erp
 *
 * @Author: lihong
 * @Date: 2018/8/24
 * @Description FIXME 客户端配置1（扫描组件）
 */
@SpringBootApplication
@ComponentScan(basePackages = {"org.lhpsn.sso.client", "com.demo.erp"})
public class ErpApplication {

    public static void main(String[] args) {
        SpringApplication.run(ErpApplication.class, args);
    }
}
