package org.jfinger.cloud.utils.common;

import org.springframework.util.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptUtils {

    //加密算法
    public static final String ALGORITHM = "PBEWithMD5AndDES";

    //迭代次数
    private static final int ITERATION_COUNT = 1000;

    /**
     * 根据PBE密码生成一把密钥
     *
     * @param password 生成密钥时所使用的密码
     * @return Key PBE算法密钥
     */
    private static Key getPBEKey(String password) {
        // 实例化使用的算法
        SecretKeyFactory keyFactory;
        SecretKey secretKey = null;
        try {
            keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            // 设置PBE密钥参数
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            // 生成密钥
            secretKey = keyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return secretKey;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 字节数组转成十六进制串
     *
     * @param bytes 字节数组
     * @return
     */
    public static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                // 1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    /**
     * 将十六进制字符串转换为字节数组
     *
     * @param hexString 十六进制字符串
     * @return
     */
    public static byte[] hex2Byte(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * base64解密
     *
     * @param str 源文本
     * @return
     */
    public static byte[] decodeBase64(String str) {
        return Base64.getDecoder().decode(str);
    }

    /**
     * base64加密
     *
     * @param data 源字节数组
     * @return
     */
    public static String encodeBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * MD5加密,返回小写
     *
     * @param s 源文本
     * @return
     */
    public static String md5(String s) {
        return md5(s, false);
    }

    /**
     * MD5加密
     *
     * @param s         源文本
     * @param uppercase 是否转大写
     * @return
     */
    public static String md5(String s, boolean uppercase) {
        try {
            String res = DigestUtils.md5DigestAsHex(s.getBytes("UTF-8"));
            return uppercase ? res.toUpperCase() : res.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * sha256
     *
     * @param secret 源串
     * @return
     */
    public static String sha256(String secret) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(secret.getBytes("UTF-8"));
            return byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建指定数量的随机字符串，字母于数字混合
     *
     * @param length 字符串长度
     * @return
     */
    public static String createRandom(int length) {
        return createRandom(false, length);
    }

    /**
     * 创建指定数量的随机字符串
     *
     * @param numberFlag 是否是数字
     * @param length     字符串长度
     * @return
     */
    public static String createRandom(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyzABCDEFGHIJKMNPQRSTUVWXYZ";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);
        return retStr;
    }

    /**
     * 对字符串s进行补齐长度，默认左补齐
     *
     * @param s      源串
     * @param maxLen 最大长度
     * @param c      补齐字符
     * @return
     */
    public static String fillChar(String s, int maxLen, char c) {
        return fillChar(s, maxLen, c, true);
    }

    /**
     * 对字符串s进行补齐长度
     *
     * @param s      源串
     * @param maxLen 最大长度
     * @param c      补齐字符
     * @param inLeft 是否左补齐，false为右补齐
     * @return
     */
    public static String fillChar(String s, int maxLen, char c, boolean inLeft) {
        if (s == null || s.length() >= maxLen)
            return s;
        for (int i = s.length(); i < maxLen; i++) {
            if (inLeft)
                s = String.valueOf(c).concat(s);
            else
                s = String.valueOf(s).concat(String.valueOf(c));
        }
        return s;
    }

    /**
     * 加密明文字符串
     *
     * @param plaintext 待加密的明文字符串
     * @param password  生成密钥时所使用的密码
     * @param salt      盐值
     * @return 加密后的密文字符串
     * @throws Exception
     */
    public static String encrypt(String plaintext, String password, String salt) {
        Key key = getPBEKey(password);
        byte[] encipheredData = null;
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt.getBytes(), ITERATION_COUNT);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            encipheredData = cipher.doFinal(plaintext.getBytes("utf-8"));
        } catch (Exception e) {
        }
        return byte2Hex(encipheredData);
    }


    /**
     * 解密密文字符串
     *
     * @param ciphertext 待解密的密文字符串
     * @param password   生成密钥时所使用的密码(如需解密,该参数需要与加密时使用的一致)
     * @param salt       盐值(如需解密,该参数需要与加密时使用的一致)
     * @return 解密后的明文字符串
     * @throws Exception
     */
    public static String decrypt(String ciphertext, String password, String salt) {
        Key key = getPBEKey(password);
        byte[] passDec = null;
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt.getBytes(), ITERATION_COUNT);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
            passDec = cipher.doFinal(hex2Byte(ciphertext));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(passDec);
    }
}
