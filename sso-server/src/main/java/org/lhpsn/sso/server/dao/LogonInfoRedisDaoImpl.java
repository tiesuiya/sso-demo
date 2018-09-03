package org.lhpsn.sso.server.dao;

import org.lhpsn.sso.server.bean.LogonInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录注册信息对象数据访问实现
 *
 * @Author: lihong
 * @Date: 2018/8/27
 * @Description
 */
@Service
public class LogonInfoRedisDaoImpl implements LogonInfoRedisDao {

    /**
     * TGT缓存 TODO 改为redis实现
     */
    private static final Map<String, List<LogonInfo>> LOGON_INFO_CACHE = new ConcurrentHashMap<>();

    @Override
    public List<LogonInfo> getLogonInfoList(String userName) {
        return LOGON_INFO_CACHE.get(userName);
    }

    @Override
    public void save(String userName, String sessionId, String url) {
        List<LogonInfo> registerInfoList = getLogonInfoList(userName);
        if (registerInfoList == null) {
            registerInfoList = new ArrayList<>();
        }

        LogonInfo registerInfo = new LogonInfo();
        registerInfo.setSessionId(sessionId);
        registerInfo.setServiceUrl(url);
        registerInfoList.add(registerInfo);

        LOGON_INFO_CACHE.put(userName, registerInfoList);
    }
}
