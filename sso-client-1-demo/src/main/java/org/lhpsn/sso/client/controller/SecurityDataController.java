package org.lhpsn.sso.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 安全数据测试控制器
 *
 * @Author: lihong
 * @Date: 2018/8/28
 * @Description
 */
@Slf4j
@Controller
public class SecurityDataController {

    @GetMapping("/data/info")
    @ResponseBody
    public Map<String, Object> bu() {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("name", "CRM");
        objectMap.put("version", "3.2.0");
        return objectMap;
    }

    @GetMapping("/data/page")
    public String bu1() {
        return "securityPage";
    }
}
