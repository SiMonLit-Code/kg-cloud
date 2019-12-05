package com.plantdata.kgcloud.domain.menu;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


/**
 * AES 加密方法，是对称的密码算法(加密与解密的密钥一致)，这里使用最大的 256 位的密钥,
 * 对外提供密钥生成、加密、解密方法
 */
@Component
public class AESUtil {

    private static final Logger log = LoggerFactory.getLogger(AESUtil.class);

    private AESUtil() {
    }

    //秘钥
    private static String aesKey = "RlZmM0NmUtZDMzYS00YThhLWIwNjUtNj";
    //向量
    private static String iv = "UyNmVhOWYtYmJkZi";


    /**
     * 加密
     *
     * @param content 待加密内容
     * @return 加密后的密文, 进行 BASE64 处理之后返回
     */
    public static String encryptAES(byte[] content) {
        try {
            // 获得一个 SecretKeySpec
            SecretKeySpec secretKeySpec = new SecretKeySpec(aesKey.getBytes(), "AES");
            // 获得加密算法实例对象 Cipher
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding"); //"算法/模式/补码方式"
            // 获得一个 IvParameterSpec
            GCMParameterSpec ivSpec = new GCMParameterSpec(128, iv.getBytes());  // 使用 CBC 模式，需要一个向量 iv, 可增加加密算法的强度
            // 根据参数初始化算法
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

            // 执行加密并返回经 BASE64 处助理之后的密文
            return Base64.getEncoder().encodeToString(cipher.doFinal(content));
        } catch (Exception e) {
            log.error("encryptAES fail...", e);
        }
        return "";
    }

    /**
     * 解密
     *
     * @param content:      待解密内容，是 BASE64 编码后的字节数组
     * @param secretKeyStr: 解密使用的 AES 密钥，BASE64 编码后的字符串
     * @param iv:           初始化向量，长度 16 字节，16*8 = 128 位
     * @return 解密后的明文，直接返回经 UTF-8 编码转换后的明文
     */
    private static String decryptAES(byte[] content, String secretKeyStr, String iv) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, UnsupportedEncodingException {
        // 密文进行 BASE64 解密处理
        byte[] contentDecByBase64 = Base64.getDecoder().decode(content);
        // 获得一个 SecretKeySpec
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyStr.getBytes(), "AES");
        // 获得加密算法实例对象 Cipher
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding"); //"算法/模式/补码方式"
        // 获得一个初始化 IvParameterSpec
        GCMParameterSpec ivSpec = new GCMParameterSpec(128, iv.getBytes());
        // 根据参数初始化算法
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
        // 解密
        return new String(cipher.doFinal(contentDecByBase64), "utf8");
    }

    public static Integer[] encryptEnid(String enid) {
        try {
            String s = AESUtil.decryptAES(enid.getBytes(), aesKey, iv);
            String[] arry = s.split("_");
            Integer[] ids = new Integer[2];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = Integer.valueOf(arry[i]);
            }
            return ids;
        } catch (Exception e) {
            log.error("encryptEnid fail...", e);
        }
        return new Integer[]{};
    }

}
