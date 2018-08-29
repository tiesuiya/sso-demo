package org.lhpsn.sso.server.bean;

import lombok.Data;

/**
 * 注册信息
 *
 * @Author: lihong
 * @Date: 2018/8/24
 * @Description
 */
@Data
public class RegisterInfo {

    private String sessionId;
    private String serviceUrl;
}
