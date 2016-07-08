package com.softdesign.devintensive.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by savos on 08.07.2016.
 */

public class SecurityHelper {

    private static MessageDigest sMessageDigest = null;

    public static String getMd5(String message){
//        try {
//            sMessageDigest = MessageDigest.getInstance("MD5");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            return "";
//        }
//        sMessageDigest.reset();
//        sMessageDigest.update(message.getBytes());
//        byte[] digest = sMessageDigest.digest();
//        return digest.toString();

        return message.hashCode() + "";

    }
}
