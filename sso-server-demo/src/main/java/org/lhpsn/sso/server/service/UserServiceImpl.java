package org.lhpsn.sso.server.service;

import org.lhpsn.sso.server.User;
import org.springframework.stereotype.Service;

/**
 * 用户service实现
 *
 * @Author: lihong
 * @Date: 2018/8/24
 * @Description
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public User get(String userName, String passWord) {
        // TODO 暂无数据库，伪代码构造一个用户对象
        if (userName.equals(passWord)) {
            User user = new User();
            user.setUserName(userName);
            user.setPassWord(passWord);
            return user;
        }
        return null;
    }
}
