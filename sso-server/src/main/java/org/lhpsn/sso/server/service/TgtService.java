package org.lhpsn.sso.server.service;

import org.lhpsn.sso.common.dto.UserDTO;
import org.lhpsn.sso.server.bean.Tgt;

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
     * @param tgc     TGC
     * @param userDTO 用户
     */
    void save(String tgc, UserDTO userDTO);

    /**
     * 删除TGT
     *
     * @param tgc TGC
     */
    void remove(String tgc);

}
