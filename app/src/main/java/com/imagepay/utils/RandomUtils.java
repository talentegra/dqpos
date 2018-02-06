package com.imagepay.utils;

import java.util.Random;

public final class RandomUtils {
    public static final String CHAR_ALL = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String CHAR_HEX = "0123456789ABCDEF";
    public static final String CHAR_LETTER = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String CHAR_NUMBER = "0123456789";

    public static String generateString(int i, String str) {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        int length = str.length();
        for (int i2 = 0; i2 < i; i2++) {
            stringBuffer.append(str.charAt(random.nextInt(length)));
        }
        return stringBuffer.toString();
    }

    public static String generateHexString(int i) {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        for (int i2 = 0; i2 < i; i2++) {
            stringBuffer.append(CHAR_HEX.charAt(random.nextInt(16)));
        }
        return stringBuffer.toString();
    }
}
