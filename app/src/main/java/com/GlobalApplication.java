package com;

import android.app.Application;

import com.dqserv.ConnectivityReceiver;

/**
 * Created by Admin on 2/12/2018.
 */

public class GlobalApplication extends Application {
    public static final String TAG = Application.class.getSimpleName();
    private static GlobalApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized GlobalApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
