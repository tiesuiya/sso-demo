package org.lhpsn.sso.server.bean;

import lombok.Data;
import org.lhpsn.sso.common.dto.UserDTO;

/**
 * Tgt对象
 *
 * @Author: lihong
 * @Date: 2018/8/29
 * @Description
 */
@Data
public class Tgt {

    private String tgc;
    private UserDTO userDTO;
}
