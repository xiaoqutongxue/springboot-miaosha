package com.imook.miaosha.util;

import java.util.UUID;

public class UUIDUtil {
    public static String uuid(){
        // 原生的uuid带横线 用这个方法去掉横线
        return UUID.randomUUID().toString().replace("-","");
    }
}
