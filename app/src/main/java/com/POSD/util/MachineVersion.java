package com.POSD.util;

import android.os.Build;

public class MachineVersion {
    public static String getMachineVersion() {
        try {
            return Build.DISPLAY.substring(0, 7);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
