package org.lhpsn.sso.server.dao;

import org.lhpsn.sso.server.bean.Tgt;
import org.lhpsn.sso.server.bean.User;

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
     * @param tgc  key
     * @param user value
     */
    void save(String tgc, User user);

    /**
     * 删除TGT
     *
     * @param tgc key
     */
    void remove(String tgc);
}
