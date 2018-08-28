package org.lhpsn.sso.server.service;

import org.lhpsn.sso.server.bean.User;

import javax.servlet.http.HttpServletResponse;

/**
 * Cas服务
 *
 * @Author: lihong
 * @Date: 2018/8/27
 * @Description
 */
public interface CasService {
    /**
     * 生成TGC
     *
     * @return TGC
     */
    String generateTGC();

    /**
     * 通过TGC生成ST
     *
     * @param tgc TGC
     * @return st
     */
    String generateST(String tgc);

    /**
     * 通过TGC获取用户
     *
     * @param tgc TGC
     * @return 用户
     */
    User getUserByTGC(String tgc);

    /**
     * 通过ST获取用户 （也用于验证ST）
     *
     * @param st ST
     * @return 用户
     */
    User getUserByST(String st);

    /**
     * cas单点登出
     *
     * @param service  登出客户端URL
     * @param tgc      TGC
     * @param response response
     */
    void logout(String service, String tgc, HttpServletResponse response);

}
