package org.lhpsn.sso.common;

/**
 * Cas 相关配置
 *
 * @Author: lihong
 * @Date: 2018/8/28
 * @Description
 */
public class CasConst {

    /**
     * 登录session
     */
    public static final String LOGGED_SESSION = "isLogin";

    /**
     * session ticket
     */
    public static final String SESSION_TICKET = "ticket";

    /**
     * TGC cookie
     */
    public static final String CASTGC_COOKIE = "CASTGC";

    /**
     * 单点登录地址
     */
    public static final String SERVER_LOGIN_PATH = "/cas/login";

    /**
     * 单点登出地址
     */
    public static final String SERVER_LOGOUT_PATH = "/cas/logout";

    /**
     * ST验证地址
     */
    public static final String SERVER_ST_VALIDATE_PATH = "/serviceValidate";

    /**
     * 客户端登出地址
     */
    public static final String CLIENT_LOGOUT_PATH = "/cas/client/logout";
}
