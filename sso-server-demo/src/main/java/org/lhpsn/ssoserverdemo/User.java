package org.lhpsn.ssoserverdemo;

import lombok.Data;

/**
 * 用户实体
 *
 * @Author: lihong
 * @Date: 2018/8/24
 * @Description
 */
@Data
public class User {

    private String userName;
    private String passWord;
    private String notes;
}
