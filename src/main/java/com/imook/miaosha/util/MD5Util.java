package com.imook.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 对明文密码进行MD5处理
 */
public class MD5Util {

    // 给MD5再进行加密
    public static final String salt = "1a2b3c4d";

    public static String md5(String src){
        return DigestUtils.md5Hex(src);

    }

    // 输入的密码转换成form表单的密码
    // 第一次MD5
    public static String inputPassToFromPass(String inputPass){
        String str = "" + salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    // form表单的密码转换成输入DB里面的密码
    // 第二次MD5
    public static String formPassToDBPass(String formPass, String salt){
        String str = "" +salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }


    // 最终插入到db中的密码
    public static String inputPassToDBPass(String input, String saltDB){
        String formPass = inputPassToFromPass(input);
        String dbPass = formPassToDBPass(formPass,saltDB);
        return dbPass;
    }

    public static void main(String[] args) {
        //System.out.println(inputPassToFromPass("123456"));
        //System.out.println(formPassToDBPass(inputPassToFromPass("123456"),"1a2b3c4d"));
        System.out.println(inputPassToDBPass("123456","1a2b3c4d"));
    }
}
