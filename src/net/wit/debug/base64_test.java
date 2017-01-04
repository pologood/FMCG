package net.wit.debug;

import org.apache.commons.codec.binary.Base64;

import net.wit.util.Base64Util;

public class base64_test {

    public static void main(String[] args) throws Exception {
        System.out.println(" client id:" + new String(Base64.encodeBase64("2200003220".getBytes()), "UTF-8"));
        System.out.println(" user id:" + new String(Base64.encodeBase64("2200003220001".getBytes()), "UTF-8"));
        System.out.println(" password:" + new String(Base64.encodeBase64("111111".getBytes()), "UTF-8"));
        System.out.println(" acct no:" + new String(Base64.encodeBase64("600033037".getBytes()), "UTF-8"));
        System.out.println(" acct name:" + new String(Base64.encodeBase64("测试2200003220".getBytes()), "UTF-8"));
        System.out.println(" acctNo:" + new String(Base64.decodeBase64("NjkzMDIzMDA2".getBytes()), "UTF-8"));
        System.out.println(" acctName:" + new String(Base64.decodeBase64("5Y2X5Lqs56Wl6L+Q55S15a2Q5ZWG5Yqh6IKh5Lu95pyJ6ZmQ5YWs5Y+4".getBytes()), "UTF-8"));
    }
}
