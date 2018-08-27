package org.lhpsn.sso.server.service;

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
     * @param gtc GTC
     * @return st
     */
    String generateST(String gtc);

    /**
     * 验证ST是否有效
     *
     * @param st ST
     * @return
     */
    Boolean validateST(String st);
}
