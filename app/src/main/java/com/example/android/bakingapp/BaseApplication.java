package com.example.android.bakingapp;

import android.app.Application;
import android.content.IntentFilter;
import com.example.android.bakingapp.network.ConnectivityReceiver;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

/**
 * Created by aditibhattacharya on 28/01/2018.
 */

public class BaseApplication extends Application {

    private static boolean mIsActivityVisible;
    private static IntentFilter mIntentFilter;
    private static ConnectivityReceiver mReceiver;
    private static BaseApplication mApplicationInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationInstance = this;
        mReceiver = new ConnectivityReceiver();

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(getString(R.string.intent_filter_connectivity_change));
        mIntentFilter.addAction(getString(R.string.intent_filter_wifi_state_changed));

        Timber.plant(new DebugTree());

        setReceiverStatus(true);
    }

    /**
     * Method that returns the application instance
     * @return ApplicationInstance
     */
    private static synchronized BaseApplication getApplicationInstance() {
        return mApplicationInstance;
    }

    /**
     * Method to set the connectivity listener
     * @param listener
     */
    public static void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.mConnectivityReceiverListener = listener;
    }

    /**
     * Method that registers/unregisters receiver based on whether the application is visible or not
     */
    public static void setReceiverStatus(boolean isActivityVisible) {
        if (isActivityVisible) {
            // register broadcast receiver
            getApplicationInstance().registerReceiver(mReceiver, mIntentFilter);
        } else {
            // unregister broadcast receiver to prevent memory leaks
            getApplicationInstance().unregisterReceiver(mReceiver);
        }
    }
}
