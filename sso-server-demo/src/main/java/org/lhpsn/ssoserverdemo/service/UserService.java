package org.lhpsn.ssoserverdemo.service;

import org.lhpsn.ssoserverdemo.User;

/**
 * 用户service
 *
 * @Author: lihong
 * @Date: 2018/8/24
 * @Description
 */
public interface UserService {
    /**
     * 获取用户
     *
     * @param userName 账号
     * @param passWord 密码
     * @return 用户
     */
    User get(String userName, String passWord);
}
