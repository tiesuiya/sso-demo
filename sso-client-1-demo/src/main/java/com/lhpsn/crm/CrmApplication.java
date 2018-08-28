package com.lhpsn.crm;

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
@ComponentScan(basePackages = {"com.lhpsn.crm", "org.lhpsn.sso.client"})
public class CrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }
}
