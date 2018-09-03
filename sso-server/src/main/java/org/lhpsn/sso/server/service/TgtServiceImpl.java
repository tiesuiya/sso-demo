package org.lhpsn.sso.server.service;

import lombok.extern.slf4j.Slf4j;
import org.lhpsn.sso.common.dto.UserDTO;
import org.lhpsn.sso.server.bean.LogonInfo;
import org.lhpsn.sso.server.bean.Tgt;
import org.lhpsn.sso.server.dao.TgtRedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * TGT服务实现
 *
 * @Author: lihong
 * @Date: 2018/8/29
 * @Description
 */
@Slf4j
@Service
public class TgtServiceImpl implements TgtService {

    @Autowired
    private TgtRedisDao tgtRedisDao;

    @Override
    public Tgt get(String tgc) {
        if (tgc != null) {
            return tgtRedisDao.getTgt(tgc);
        }
        return null;
    }

    @Override
    public void save(String tgc, UserDTO userDTO) {
        tgtRedisDao.save(tgc, userDTO);
    }

    @Override
    public void remove(String tgc) {
        if (tgc != null) {
            tgtRedisDao.remove(tgc);
        }
    }
}
