package com.Cognaxon;

public class WSQdemo {
    public static native byte[] WSQ_decode_stream(byte[] bArr, int[] iArr);

    public static native byte[] WSQ_encode_stream(byte[] bArr, int i, int i2, double d, int i3, String str);

    static {
        System.loadLibrary("WSQ_library_android");
    }
}
