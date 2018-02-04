package com.example.android.bakingapp.utils;

/**
 * This class is used to store global constants used throughout the app.
 * Created by aditibhattacharya on 25/01/2018.
 */

public class Config {

    // Recipe URL
    public static final String RECIPE_BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    // Duration in Milliseconds after which connection times out
    public static final int DURATION_CONNECTION_TIMEOUT = 100000;

    // Value used to check if JSON has the required data
    public static final byte JSON_BYTE_VALUE = 0x01;

    // Intent Extra Keys
    public static final String INTENT_EXTRA_SELECTED_RECIPE = "selected_recipe";
}
