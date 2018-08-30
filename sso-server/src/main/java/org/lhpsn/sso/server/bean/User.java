package org.lhpsn.sso.server.bean;

import lombok.Data;

import java.util.List;

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
    private String notes;
    private List<RegisterInfo> registerInfoList;
}
