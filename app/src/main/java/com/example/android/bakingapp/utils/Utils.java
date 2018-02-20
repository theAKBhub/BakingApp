package com.example.android.bakingapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import butterknife.ButterKnife;

/**
 * This class contains common methods.
 * Created by aditibhattacharya on 25/01/2018.
 */

public class Utils {

    /**
     * This is a private constructor and only meant to hold static variables and methods,
     * which can be accessed directly from the class name Utils
     */
    private void Utils() {}

    /**
     * Utility method to construct a Toast message
     * @param context
     * @param toast
     * @param message
     * @return Toast object
     */
    public static Toast showToastMessage(Context context, Toast toast, String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        return toast;
    }

    /**
     * Utility method to check if a string is empty or not
     * @param stringToCheck
     * @return TRUE (if empty string) / FALSE
     */
    public static boolean isEmptyString(String stringToCheck) {
        return (stringToCheck == null || stringToCheck.trim().length() == 0);
    }

    /**
     * Utility method to check if a number is non-zero
     * @param numToCheck
     * @return TRUE (if number is zero) / FALSE
     */
    public static boolean isNumZero(int numToCheck) {
        return (numToCheck == 0);
    }

    /**
     * Utility Method to check if device has got connectivity
     * @param context
     * @return TRUE (if connected) / FALSE
     */
    public static boolean hasConnectivity(Context context) {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Utility method to convert first letter of a string to Uppercase
     * @param word
     * @return word with first letter uppercase
     */
    public static String convertStringToFirstCapital (String word) {
        return word.toUpperCase().charAt(0) + word.substring(1).toLowerCase();
    }

    /**
     * Utility method to convert string to Title Case
     * @param inputString
     * @return string in Title Case
     */
    public static String convertStringToTitleCase (String inputString) {
        String [] wordsArray = inputString.split(" ");
        for (int i = 0; i < wordsArray.length; i++) {
            wordsArray[i] = convertStringToFirstCapital(wordsArray[i]);
        }

        return TextUtils.join(" ", wordsArray);
    }


    /**
     * Butterknife setter interface to change view visibility
     */
    public static final ButterKnife.Setter<View, Integer> VISIBILITY = new
            ButterKnife.Setter<View,Integer>() {
        @Override
        public void set(@NonNull View view, Integer value, int index) {
            view.setVisibility(value);
        }
    };

}
