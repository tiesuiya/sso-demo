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
     * 获取
     *
     * @param tgc
     * @return
     */
    Tgt get(String tgc);

    void save(String tgc, User user);
}
