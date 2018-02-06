package com.POSD;

import android.content.Context;
import android.content.SharedPreferences;
import com.POSD.util.LogUtil;
import com.POSD.util.ResourceUtil;

public class Preference {
    private static final String TAG = Preference.class.getSimpleName();

    private static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(context.getString(ResourceUtil.getStringResIDByName(context, "app_name")), 0);
    }

    public static boolean isScreenOn(Context context) {
        return getSP(context).getBoolean("IsScreenOn", true);
    }

    public static void setScreenOn(Context context, boolean value) {
        getSP(context).edit().putBoolean("IsScreenOn", value).commit();
    }

    public static int getScannerModel(Context context) {
        return getSP(context).getInt("ScannerModel", -1);
    }

    public static void setScannerModel(Context context, int value) {
        getSP(context).edit().putInt("ScannerModel", value).commit();
    }

    public static int getScannerPrefix(Context context) {
        return getSP(context).getInt("ScannerPrefix", -1);
    }

    public static void setScannerPrefix(Context context, int value) {
        getSP(context).edit().putInt("ScannerPrefix", value).commit();
    }

    public static boolean getScannerIsReturnFactory(Context context) {
        return getSP(context).getBoolean("IsReturnFactory", false);
    }

    public static void setScannerIsReturnFactory(Context context, boolean isReturnFactory) {
        getSP(context).edit().putBoolean("IsReturnFactory", isReturnFactory).commit();
    }

    public static int getScanDeviceType(Context context) {
        return getSP(context).getInt("ScanDeviceType", 1);
    }

    public static void setScanDeviceType(Context context, int value) {
        getSP(context).edit().putInt("ScanDeviceType", value).commit();
    }

    public static int getScanOutMode(Context context) {
        return getSP(context).getInt("ScanOutMode", 1);
    }

    public static void setScanOutMode(Context context, int ScanOutMode) {
        getSP(context).edit().putInt("ScanOutMode", ScanOutMode).commit();
    }

    public static void setNetPageSupport(Context context, boolean isNetPageSupport) {
        getSP(context).edit().putBoolean("IsNetPageSupport", isNetPageSupport).commit();
    }

    public static boolean getNetPageSupport(Context context, boolean defaultValues) {
        return getSP(context).getBoolean("IsNetPageSupport", defaultValues);
    }

    public static void setScanSound(Context context, boolean isScanSound) {
        getSP(context).edit().putBoolean("IsScanSound", isScanSound).commit();
    }

    public static boolean getScanSound(Context context, boolean defaultValues) {
        return getSP(context).getBoolean("IsScanSound", defaultValues);
    }

    public static void setScanVibration(Context context, boolean isScanVibration) {
        getSP(context).edit().putBoolean("IsScanVibration", isScanVibration).commit();
    }

    public static boolean getScanVibration(Context context, boolean defaultValues) {
        return getSP(context).getBoolean("IsScanVibration", defaultValues);
    }

    public static void setScanSaveTxt(Context context, boolean isScanSaveTxt) {
        getSP(context).edit().putBoolean("IsScanSaveTxt", isScanSaveTxt).commit();
    }

    public static boolean getScanSaveTxt(Context context, boolean defaultValues) {
        return getSP(context).getBoolean("IsScanSaveTxt", defaultValues);
    }

    public static void setScanTest(Context context, boolean isScanSaveTxt) {
        getSP(context).edit().putBoolean("IsScanTest", isScanSaveTxt).commit();
    }

    public static boolean getScanTest(Context context) {
        return getSP(context).getBoolean("IsScanSaveTxt", false);
    }

    public static void setScanShortcutSupport(Context context, boolean isScanShortcutSupport) {
        getSP(context).edit().putBoolean("IsScanShortcutSupport", isScanShortcutSupport).commit();
    }

    public static boolean getScanShortcutSupport(Context context, boolean defaultValues) {
        return getSP(context).getBoolean("IsScanShortcutSupport", defaultValues);
    }

    public static void setScanShortcutMode(Context context, String scanShortcutMode) {
        getSP(context).edit().putString("ScanShortcutMode", scanShortcutMode).commit();
    }

    public static String getScanShortcutMode(Context context, String defaultValues) {
        return getSP(context).getString("ScanShortcutMode", defaultValues);
    }

    public static void setScanShortCutPressMode(Context context, int scanShortCutPressMode) {
        getSP(context).edit().putInt("ScanShortCutPressMode", scanShortCutPressMode).commit();
    }

    public static int getScanShortCutPressMode(Context context) {
        return getSP(context).getInt("ScanShortCutPressMode", 1);
    }

    public static void setScanSelfopenSupport(Context context, boolean isScanSelfopenSupport) {
        getSP(context).edit().putBoolean("IsScanSelfopenSupport", isScanSelfopenSupport).commit();
    }

    public static boolean getScanSelfopenSupport(Context context, boolean defaultValues) {
        return getSP(context).getBoolean("IsScanSelfopenSupport", defaultValues);
    }

    public static void setScanSuffixModel(Context context, int suffixModel) {
        if (suffixModel >= -1 && suffixModel <= 1) {
            getSP(context).edit().putInt("ScanSuffixModel", suffixModel).commit();
        }
    }

    public static int getScanSuffixModel(Context context, int defalutValue) {
        if (defalutValue < -1 || defalutValue > 1) {
            return getSP(context).getInt("ScanSuffixModel", 0);
        }
        return getSP(context).getInt("ScanSuffixModel", defalutValue);
    }

    public static void setNfcBackgroundSupport(Context context, boolean isScanSelfopenSupport) {
        getSP(context).edit().putBoolean("IsScanSelfopenSupport", isScanSelfopenSupport).commit();
    }

    public static boolean getNfcBackgroundSupport(Context context, boolean defaultValues) {
        return getSP(context).getBoolean("IsScanSelfopenSupport", defaultValues);
    }

    public static void setNfcSimulateKeySupport(Context context, boolean isNfcSimulateKeySupport) {
        getSP(context).edit().putBoolean("IsNfcSimulateKeySupport", isNfcSimulateKeySupport).commit();
    }

    public static boolean getNfcSimulateKeySupport(Context context, boolean defaultValues) {
        return getSP(context).getBoolean("IsNfcSimulateKeySupport", defaultValues);
    }

    public static void setIsFirstStartNot(Context context) {
        getSP(context).edit().putBoolean("IsFirstStart", false).commit();
    }

    public static boolean getIsFirstStartNot(Context context) {
        return getSP(context).getBoolean("IsFirstStart", true);
    }

    public static void setScanInit(Context context, boolean isture) {
        LogUtil.m12d(TAG, "setScanInit: parameter is " + isture);
        getSP(context).edit().putBoolean("IsScanInit", isture).commit();
    }

    public static boolean getIsScanInit(Context context) {
        return getSP(context).getBoolean("IsScanInit", false);
    }
}
