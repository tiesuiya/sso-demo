package org.lhpsn.sso.server.dao;

import org.lhpsn.sso.server.bean.LogonInfo;

import java.util.List;

/**
 * 登录信息数据访问类
 *
 * @Author: lihong
 * @Date: 2018/8/27
 * @Description
 */
public interface LogonInfoRedisDao {
    /**
     * 获取登录注册信息
     *
     * @param userName
     * @return
     */
    List<LogonInfo> getLogonInfoList(String userName);

    /**
     * 保存登录注册信息
     *
     * @param userName  用户名
     * @param sessionId 客户端sessionId
     * @param url       客户端url
     */
    void save(String userName, String sessionId, String url);
}
