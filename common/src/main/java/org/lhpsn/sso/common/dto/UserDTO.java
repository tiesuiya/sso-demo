package org.lhpsn.sso.common.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户DTO
 *
 * @Author: lihong
 * @Date: 2018/9/3
 * @Description
 */
@Data
public class UserDTO {
    private String userName;

    private List<RoleDTO> roleDTOList;

    private List<PermissionDTO> permissionList;

    public UserDTO(String userName, List<RoleDTO> roleDTOList, List<PermissionDTO> permissionList) {
        this.userName = userName;
        this.roleDTOList = roleDTOList;
        this.permissionList = permissionList;
    }
}
