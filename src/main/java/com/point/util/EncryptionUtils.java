package com.point.util;

import com.point.constant.Constant;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by 肥肥 on 2017/8/20 0020.
 */
public class EncryptionUtils {


    /**
     * AES算法加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码 （长度要是8的倍数）
     * @return
     */
    public static byte[] aesCrypto(String content, String password, boolean isRandom) {
        try {

            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");        //此处linux与windows不同， 需要注意！！！
            random.setSeed(password.getBytes());

            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器

            if (isRandom) {
                //修改原str 防止加密后重复问题
                content = PublicUtil.getAddSpaceStr(content);
            }

            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return result; // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * AES算法解密
     *
     * @param content  待解密内容
     * @param password 解密密钥 （长度要是8的倍数）
     * @return
     */
    public static byte[] aesDecrypt(byte[] content, String password) {
        try {


            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());

            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, random);

            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 加密
     *
     * @param temp
     * @param isRandom true：随机加密   false：固定加密
     * @return
     */
    public static String encrypt(String temp, boolean isRandom) {
        byte[] encryptResult = aesCrypto(temp, Constant.EncryptKey, isRandom);
        String encryptResultStr = parseByte2HexStr(encryptResult);
        return encryptResultStr;
    }


    /**
     * 解密
     *
     * @return
     */
    public static String decrypt(String temp) {
        try {
            byte[] decryptFrom = parseHexStr2Byte(temp);
            byte[] decryptResult = aesDecrypt(decryptFrom, Constant.EncryptKey);
            return new String(decryptResult).trim();
        } catch (Exception e) {
            return null;
        }

    }

//    public static void main(String[] args) {
//
//        String a = "36F17C3939AC3E7B2FC9396FA8E953EA|asdasdas-eweqwe123";
//
//        //       String b = encrypt(a,true);
////        String c = encrypt(a,false);
////
////
//        String d = decrypt(a);
////        String e = decrypt(c);
////
////        System.out.println(a);
////        System.out.println(b);
////        System.out.println(c);
//        System.out.println(d);
////        System.out.println(e);
//
//
//    }
}
