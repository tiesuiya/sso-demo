package org.lhpsn.sso.server.service;


import lombok.extern.slf4j.Slf4j;
import org.lhpsn.sso.server.util.DesUtils;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;


/**
 * Cas服务实现
 *
 * @Author: lihong
 * @Date: 2018/8/27
 * @Description
 */
@Slf4j
@Service
public final class CasServiceImpl extends DesUtils implements CasService {

    @Override
    public String generateTGC() {
        String prefix = "TGC";
        return prefix + UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    @Override
    public String generateST(String tgc) {
        // 保证唯一，加盐，验证的时候去掉就行
        String saltGtc = tgc + getSalt();
        return DesUtils.encrypt(saltGtc, KEY);
    }

    @Override
    public String getTGCByST(String st) {
        // 通过ST解密，得到加盐TGC
        String saltTgc = decrypt(st, KEY);

        // 去掉附加盐值
        return saltTgc.substring(0, saltTgc.length() - getSaltLength());
    }

    /**
     * 返回盐值长度
     *
     * @return 随机数
     */
    private static int getSaltLength() {
        return getSalt().length();
    }

    /**
     * 生成固定长度的随机数盐值
     *
     * @return 随机数
     */
    private static String getSalt() {
        // 4位
        return String.valueOf(new Random().nextInt(9000) + 1000);
    }

}
