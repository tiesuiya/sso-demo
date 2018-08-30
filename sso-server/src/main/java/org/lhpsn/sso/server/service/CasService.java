package org.lhpsn.sso.server.service;

import org.lhpsn.sso.server.bean.User;

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
     * 通过ST获取TGC
     *
     * @param st ST
     * @return TGC
     */
    String getTGCByST(String st);

}
