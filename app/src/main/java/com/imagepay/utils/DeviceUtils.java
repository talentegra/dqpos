package com.imagepay.utils;

import android.content.Context;
import android.media.AudioRecord;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;

public final class DeviceUtils {
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getReleaseVersion() {
        return VERSION.RELEASE;
    }

    public static int getSDKVersion() {
        return VERSION.SDK_INT;
    }

    public static String getDeviceId(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
    }

    public static String getDeviceSoftwareVersion(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getDeviceSoftwareVersion();
    }

    public static boolean isMICAvailable() {
        try {
            boolean z;
            int minBufferSize = AudioRecord.getMinBufferSize(8000, 16, 2);
            if (minBufferSize <= 0) {
                minBufferSize = 2096;
            }
            AudioRecord audioRecord = new AudioRecord(1, 8000, 16, 2, minBufferSize);
            if (audioRecord.getState() == 1) {
                z = true;
            } else {
                z = false;
            }
            audioRecord.release();
            return z;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isEmulator(Context context) {
        return "000000000000000".equalsIgnoreCase(getDeviceId(context));
    }
}
