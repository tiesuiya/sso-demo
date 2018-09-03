package org.lhpsn.sso.common.dto;

import lombok.Data;

/**
 * 权限DTO
 *
 * @Author: lihong
 * @Date: 2018/9/3
 * @Description
 */
@Data
public class PermissionDTO {
    private String name;

    public PermissionDTO(String name) {
        this.name = name;
    }
}
