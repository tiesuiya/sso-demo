package com.lhpsn.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 模拟crm
 *
 * @Author: lihong
 * @Date: 2018/8/24
 * @Description
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.lhpsn.erp", "org.lhpsn.sso.client"})
public class CrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }
}
