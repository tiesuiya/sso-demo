package org.lhpsn.sso.common.util;

import org.lhpsn.sso.common.dto.PermissionDTO;
import org.lhpsn.sso.common.dto.RoleDTO;
import org.lhpsn.sso.common.dto.UserDTO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * DTO伪数据Mock工具类
 *
 * @Author: lihong
 * @Date: 2018/9/3
 * @Description
 */
public final class DTOMockUtils {
    private DTOMockUtils() {
    }

    private static final Map<String, UserDTO> MOCK_USER_LIST = new HashMap<>();

    static {
        MOCK_USER_LIST.put("ruige", new UserDTO("ruige", Arrays.asList(
                new RoleDTO("经理"),
                new RoleDTO("业务员")), Arrays.asList(
                new PermissionDTO("添加订单"),
                new PermissionDTO("删除订单"))));
        MOCK_USER_LIST.put("yangjie", new UserDTO("yangjie", Arrays.asList(
                new RoleDTO("财务")), Arrays.asList(
                new PermissionDTO("转账"),
                new PermissionDTO("提款"))));
        MOCK_USER_LIST.put("huangzong", new UserDTO("huangzong", Arrays.asList(
                new RoleDTO("业务员")), Arrays.asList(
                new PermissionDTO("添加订单"))));
    }

    public static UserDTO getUserData(String userName) {
        return MOCK_USER_LIST.get(userName);
    }
}
