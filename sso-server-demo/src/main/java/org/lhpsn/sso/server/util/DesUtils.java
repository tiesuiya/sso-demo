package org.lhpsn.sso.server.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * DES对称加解密工具类
 *
 * @Author: chenssy
 * @Date: 2016年5月20日 下午5:19:00
 * @Description https://github.com/chenssy89/jutils/blob/master/src/main/java/com/JUtils/encrypt/DesUtils.java
 */
public class DesUtils {

    /**
     * 默认key
     */
    protected final static String KEY = "M_1y1$1E1c1R131T1d131$1k1E1Y";

    /**
     * DES加密
     *
     * @param data 待加密字符串
     * @param key  校验位
     * @return 加密的字符串
     */
    protected static String encrypt(String data, String key) {
        String encryptedData;
        try {
            // DES算法要求有一个可信任的随机数源  
            SecureRandom sr = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象  
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKey);
            // 加密对象  
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);
            // 加密，并把字节数组编码成字符串  
            encryptedData = new sun.misc.BASE64Encoder().encode(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("加密错误，错误信息：", e);
        }
        return encryptedData;
    }

    /**
     * DES解密
     *
     * @param cryptData 待解密密文
     * @param key       校验位
     * @return 解密的字符串
     */
    protected static String decrypt(String cryptData, String key) {
        String decryptedData;
        try {
            // DES算法要求有一个可信任的随机数源  
            SecureRandom sr = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象  
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKey);
            // 解密对象  
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
            // 把字符串解码为字节数组，并解密
            decryptedData = new String(cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(cryptData)));
        } catch (Exception e) {
            throw new RuntimeException("解密错误，错误信息：", e);
        }
        return decryptedData;
    }

}
