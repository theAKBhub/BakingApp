package com.example.android.bakingapp.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Utils;

/**
 * This receiver class is used to notify whenever there is a change in network/internet connection
 * Created by aditibhattacharya on 28/01/2018.
 */

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener mConnectivityReceiverListener;


    public interface ConnectivityReceiverListener {
        void onConnectivityChanged(boolean isConnected);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction;

        if (mConnectivityReceiverListener != null) {
            intentAction = intent.getAction();
            if (!Utils.isEmptyString(intentAction)) {
                if (intentAction.equals(context.getString(R.string.intent_filter_connectivity_change)) ||
                        intentAction.equals(context.getString(R.string.intent_filter_wifi_state_changed))) {
                    mConnectivityReceiverListener.onConnectivityChanged(Utils.hasConnectivity(context));
                }
            }
        }
    }
}
