package com.dqserv;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.GlobalApplication;

import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Admin on 1/1/2018.
 */

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;
    boolean isConnected;
    static boolean isCheckConnected;

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isConnected = isAbleToConnect("http://www.google.com", 1000);
                }
            }).start();
        }

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }

    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) GlobalApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isCheckConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isCheckConnected) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isCheckConnected = isAbleToConnect("http://www.google.com", 1000);
                }
            }).start();
        }
        return isCheckConnected;
    }

    private static boolean isAbleToConnect(String url, int timeout) {
        try {
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

    // Add Code For check internet using recevicer
    /*@Override
    public void onResume() {
        super.onResume();
        // register connection status listener
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
        registerReceiver(connectivityReceiver, intentFilter);

        //register connection status listener
        CharityApp.getInstance().setConnectivityListener(this);
    }*/

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    /*@Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            Toast.makeText(getApplicationContext(), "Connect", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Not Connect", Toast.LENGTH_SHORT).show();
        }
    }*/
}