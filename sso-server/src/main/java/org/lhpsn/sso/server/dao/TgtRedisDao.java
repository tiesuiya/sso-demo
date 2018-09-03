package org.lhpsn.sso.server.dao;

import org.lhpsn.sso.common.dto.UserDTO;
import org.lhpsn.sso.server.bean.Tgt;

/**
 * TGT对象数据访问类
 *
 * @Author: lihong
 * @Date: 2018/8/27
 * @Description
 */
public interface TgtRedisDao {
    /**
     * 获取用户对象
     *
     * @param tgc key
     * @return 保存的用户对象
     */
    Tgt getTgt(String tgc);

    /**
     * 保存TGT对象
     *
     * @param tgc     key
     * @param userDTO value
     */
    void save(String tgc, UserDTO userDTO);

    /**
     * 删除TGT
     *
     * @param tgc key
     */
    void remove(String tgc);
}
