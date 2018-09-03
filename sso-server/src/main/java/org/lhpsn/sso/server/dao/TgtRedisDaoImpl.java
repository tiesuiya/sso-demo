package org.lhpsn.sso.server.dao;

import org.lhpsn.sso.common.dto.UserDTO;
import org.lhpsn.sso.server.bean.Tgt;
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
    private static final Map<String, Tgt> TGT_CACHE = new ConcurrentHashMap<>();

    @Override
    public Tgt getTgt(String tgc) {
        return TGT_CACHE.get(tgc);
    }

    @Override
    public void save(String tgc, UserDTO userDTO) {
        Tgt tgt = new Tgt();
        tgt.setTgc(tgc);
        tgt.setUserDTO(userDTO);
        TGT_CACHE.put(tgc, tgt);
    }


    @Override
    public void remove(String tgc) {
        TGT_CACHE.remove(tgc);
    }
}
