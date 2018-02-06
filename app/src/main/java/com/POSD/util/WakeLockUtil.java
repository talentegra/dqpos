package com.POSD.util;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class WakeLockUtil {
    private static final String TAG = "WakeLockUtil";
    private WakeLock mWakeLock = null;
    private PowerManager pManager = null;

    public WakeLockUtil(Context context) {
        this.pManager = (PowerManager) context.getSystemService("power");
        this.mWakeLock = this.pManager.newWakeLock(268435482, TAG);
        this.mWakeLock.setReferenceCounted(false);
    }

    public void lock() {
        if (this.mWakeLock != null) {
            this.mWakeLock.acquire(15000);
        }
    }

    public void unLock() {
        if (this.mWakeLock != null) {
            this.mWakeLock.release();
        }
    }
}
