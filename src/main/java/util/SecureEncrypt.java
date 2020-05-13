package util;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;

/**
 * @author zk
 */
public class SecureEncrypt {
    public static void main(String[] args) {
        DES _des  = new DES(Mode.CTS, Padding.PKCS5Padding, "0CoJUm6Qyw8W8zxk".getBytes(), "01044169".getBytes());
        System.out.println("$url: " + _des.encryptBase64(_des.encryptHex("jdbc:mysql://rm-wz9675t5l2wxdfjr0eo.mysql.rds.aliyuncs.com:3306/vsdwq?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8.{zk@yd}")));
        System.out.println("$user: " + _des.encryptBase64(_des.encryptBase64("weishi2019.{zk@yd}")));
        System.out.println("$pass: " + _des.encryptBase64(_des.encryptHex("Weishi668!.{zk@yd}")));
        System.out.println("$driver: " + _des.encryptBase64(_des.encryptBase64("com.mysql.jdbc.Driver.{zk@yd}")));

    }
}
