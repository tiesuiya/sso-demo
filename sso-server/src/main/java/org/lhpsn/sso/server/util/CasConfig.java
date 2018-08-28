package org.lhpsn.sso.server.util;

/**
 * Cas 相关配置
 *
 * @Author: lihong
 * @Date: 2018/8/28
 * @Description
 */
public interface CasConfig {

    /**
     * TGC cookie name
     */
    String CASTGC_COOKIE_NAME = "CASTGC";

    /**
     * 客户端登出地址
     */
    String CLIENT_LOGOUT_PATH = "/cas/client/logout";
}
