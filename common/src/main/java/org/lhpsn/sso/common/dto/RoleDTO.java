package org.lhpsn.sso.common.dto;

import lombok.Data;

/**
 * 角色DTO
 *
 * @Author: lihong
 * @Date: 2018/9/3
 * @Description
 */
@Data
public class RoleDTO {
    private String name;

    public RoleDTO(String name) {
        this.name = name;
    }
}
