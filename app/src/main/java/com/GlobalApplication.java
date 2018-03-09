package com;

import android.app.Application;
import android.content.SharedPreferences;

import com.dqserv.ConnectivityReceiver;

/**
 * Created by Admin on 2/12/2018.
 */

public class GlobalApplication extends Application {

    public static final String TAG = Application.class.getSimpleName();
    private static GlobalApplication mInstance;
    public static SharedPreferences taxPref, cgstPref, sgstPref;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        taxPref = getApplicationContext()
                .getSharedPreferences("tax_val", MODE_PRIVATE);
        cgstPref = getApplicationContext()
                .getSharedPreferences("cgst", MODE_PRIVATE);
        sgstPref = getApplicationContext()
                .getSharedPreferences("sgst", MODE_PRIVATE);
    }

    public static synchronized GlobalApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
