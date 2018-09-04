package org.lhpsn.sso.common.dto;

import lombok.Data;

/**
 * 票据验证DTO
 *
 * @Author: lihong
 * @Date: 2018/9/4
 * @Description
 */
@Data
public class ValidateDTO {
    /**
     * 验票结果
     */
    private Boolean success;

    /**
     * 用户数据
     */
    private UserDTO userDTO;
}
