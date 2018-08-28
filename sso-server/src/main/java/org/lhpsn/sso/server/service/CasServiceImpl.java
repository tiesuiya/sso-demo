package org.lhpsn.sso.server.service;


import lombok.extern.slf4j.Slf4j;
import org.lhpsn.sso.common.CasConst;
import org.lhpsn.sso.common.util.HttpUtils;
import org.lhpsn.sso.server.bean.User;
import org.lhpsn.sso.server.dao.ServiceUrlRedisDao;
import org.lhpsn.sso.server.dao.TgtRedisDao;
import org.lhpsn.sso.server.util.DesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    private TgtRedisDao tgtRedisDao;

    @Autowired
    private ServiceUrlRedisDao serviceUrlRedisDao;

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
    public User getUserByTGC(String tgc) {
        return tgtRedisDao.getUser(tgc);
    }

    @Override
    public User getUserByST(String st) {
        // 通过ST解密，得到TGC
        String saltTgc = decrypt(st, KEY);

        // 去掉附加盐值
        String tgc = saltTgc.substring(0, saltTgc.length() - getSaltLength());
        return tgtRedisDao.getUser(tgc);
    }

    @Override
    public void logout(String service, String tgc, HttpServletResponse response) {
        // 删除TGC
        Cookie cookie = new Cookie(CasConst.CASTGC_COOKIE_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        // 删除TGT
        tgtRedisDao.remove(tgc);

        // 删除session（客户端）
        for (String url : serviceUrlRedisDao.getAll()) {
            if (!StringUtils.isEmpty(url)) {
                HttpUtils.sendGet(url + CasConst.CLIENT_LOGOUT_PATH, "");
            }
        }
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
