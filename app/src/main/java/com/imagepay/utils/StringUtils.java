package com.imagepay.utils;

import android.support.v4.view.MotionEventCompat;

public final class StringUtils {
    public static String convertHexToBinary(String str) {
        String toBinaryString = Long.toBinaryString(Long.parseLong(str, 16));
        int length = str.length() * 4;
        StringBuffer stringBuffer = new StringBuffer();
        int length2 = length - toBinaryString.length();
        for (length = 1; length <= length2; length++) {
            stringBuffer.append("0");
        }
        return stringBuffer.toString() + toBinaryString;
    }

    public static byte[] convertHexToBytes(String str) {
        String toUpperCase = str.toUpperCase();
        int length = toUpperCase.length() / 2;
        char[] toCharArray = toUpperCase.toCharArray();
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            bArr[i] = (byte) ((((byte) RandomUtils.CHAR_HEX.indexOf(toCharArray[i * 2])) << 4) | ((byte) RandomUtils.CHAR_HEX.indexOf(toCharArray[(i * 2) + 1])));
        }
        return bArr;
    }

    public static String convertBytesToHex(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (byte b : bArr) {
            String toHexString = Integer.toHexString(b & MotionEventCompat.ACTION_MASK);
            if (toHexString.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(toHexString);
        }
        return stringBuilder.toString();
    }

    public static String convertStringToHex(String str) {
        char[] toCharArray = str.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        for (char toHexString : toCharArray) {
            stringBuffer.append(Integer.toHexString(toHexString));
        }
        return stringBuffer.toString();
    }

    public static String hexToStringGBK(String str) {
        if (str == null) {
            return "";
        }
        byte[] bArr = new byte[(str.length() / 2)];
        int i = 0;
        while (i < bArr.length) {
            try {
                bArr[i] = (byte) (Integer.parseInt(str.substring(i * 2, (i * 2) + 2), 16) & MotionEventCompat.ACTION_MASK);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        try {
            return new String(bArr, "GBK");
        } catch (Exception e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public static String covertHexToASCII(String str) {
        if (str == null) {
            return null;
        }
        try {
            String[] split = str.replaceAll("..", "$0 ").split(" ");
            StringBuffer stringBuffer = new StringBuffer();
            for (String parseInt : split) {
                stringBuffer.append((char) Integer.parseInt(parseInt, 16));
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
