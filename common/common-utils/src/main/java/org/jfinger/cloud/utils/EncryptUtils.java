package org.jfinger.cloud.utils;

import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptUtils {

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
     * 创建指定数量的随机字符串
     *
     * @param numberFlag 是否是数字
     * @param length
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

}
