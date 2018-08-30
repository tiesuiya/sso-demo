package org.lhpsn.sso.server.service;

import org.lhpsn.sso.server.bean.Tgt;
import org.lhpsn.sso.server.bean.User;

/**
 * TGT服务
 *
 * @Author: lihong
 * @Date: 2018/8/29
 * @Description
 */
public interface TgtService {

    /**
     * 获取TGT
     *
     * @param tgc TGC
     * @return TGT
     */
    Tgt get(String tgc);

    /**
     * 保存TGT
     *
     * @param tgc  TGC
     * @param user 用户
     */
    void save(String tgc, User user);

    /**
     * 删除TGT
     *
     * @param tgc TGC
     */
    void remove(String tgc);

    /**
     * 注册登录信息
     *
     * @param tgc       TGC
     * @param service   服务url
     * @param sessionId sessionId
     */
    void registerLoginInfo(String tgc, String service, String sessionId);
}
