package org.lhpsn.sso.server.dao;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * service地址数据访问实现
 *
 * @Author: lihong
 * @Date: 2018/8/27
 * @Description
 */
@Service
public class ClientRedisDaoImpl implements ServiceUrlRedisDao {

    /**
     * service地址缓存 TODO 改为redis实现
     */
    private static final Set<String> SERVICE_URL_CACHE = new HashSet<>();

    @Override
    public void add(String service) {
        SERVICE_URL_CACHE.add(service);
    }

    @Override
    public List<String> getAll() {
        return new ArrayList<>(SERVICE_URL_CACHE);
    }
}
