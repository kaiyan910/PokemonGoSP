package com.crookk.pkmgosp.core.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

public class Base64Utils {

    public static String encode(String plainText) {
        try {
            return Base64.encodeToString(plainText.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            LogUtils.error(Base64Utils.class, e);
        }
        return null;
    }

    public static String decode(String cipherText) {
        String plainText = "";

        try {

            byte[] data = Base64.decode(cipherText, Base64.DEFAULT);
            plainText = new String(data, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            LogUtils.error(Base64Utils.class, e);
        }
        return plainText;
    }

}
