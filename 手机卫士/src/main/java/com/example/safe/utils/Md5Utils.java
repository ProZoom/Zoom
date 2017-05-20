package com.example.safe.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {

    public static String md5(String str) {
        StringBuilder mess = new StringBuilder();

        try {
            //获取MD5密码加密器
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] bytes = str.getBytes();
            byte[] digest = md.digest(bytes);


            for (byte b : digest) {
                int d = b & 0xff;//把每个字节转换成16进制
                String hexString = Integer.toHexString(d);
                if (hexString.length() == 1) {//字节的高四位为0
                    hexString = "0" + hexString;
                }
                mess.append(hexString);//拼接
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return mess + "";
    }
}
