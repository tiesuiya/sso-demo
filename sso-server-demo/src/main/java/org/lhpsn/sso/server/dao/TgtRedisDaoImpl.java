package org.lhpsn.sso.server.dao;

import org.lhpsn.sso.server.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TGT对象数据访问实现
 *
 * @Author: lihong
 * @Date: 2018/8/27
 * @Description
 */
@Service
public class TgtRedisDaoImpl implements TgtRedisDao {

    /**
     * TGT缓存 TODO 改为redis实现
     */
    private static final Map<String, User> TGT_CACHE = new ConcurrentHashMap<>();

    @Override
    public void add(String tgc, User user) {
        TGT_CACHE.put(tgc, user);
    }

    @Override
    public User getUser(String tgc) {
        return TGT_CACHE.get(tgc);
    }
}
