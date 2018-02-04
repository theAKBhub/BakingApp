package com.example.android.bakingapp.network;

import android.content.Context;
import com.example.android.bakingapp.exceptions.NoConnectivityException;
import com.example.android.bakingapp.utils.Config;
import com.example.android.bakingapp.utils.Utils;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The purpose of the class is used to issue network requests to the URL used to retrieve data
 * Created by aditibhattacharya on 27/01/2018.
 */

public class RetrofitController {

    private static Retrofit mRetrofit = null;

    public static Retrofit getClient(Context context) throws NoConnectivityException {

        // Check if device has connection, else throw exception error and exit early
        if (!Utils.hasConnectivity(context)) {
            throw new NoConnectivityException();
        }

        if (mRetrofit == null) {
            //Create OkhttpClient.Builder object
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

            // Create HttpLoggingInterceptor object and set logging level
            HttpLoggingInterceptor httpLoggingInterceptor =  new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);  // BASIC prints request methods and response codes

            // Couple OkhttpClient.Builder object with HttpLoggingInterceptor object,
            // and set connection timeout duration
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
            okHttpClientBuilder.connectTimeout(Config.DURATION_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);

            // Create Retrofit object and attach OkHttp client to it
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Config.RECIPE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClientBuilder.build())
                    .build();
        }

        return mRetrofit;
    }

}
