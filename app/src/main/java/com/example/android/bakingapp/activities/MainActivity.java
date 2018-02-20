package com.example.android.bakingapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.android.bakingapp.BaseApplication;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.RecipeListFragment;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.network.ConnectivityReceiver;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    final Context mContext = this;
    private RecipeListFragment mRecipeListFragment;
    public static ArrayList<Recipe> sRecipeList;

    @BindView(R.id.coordinator_layout)                          CoordinatorLayout mCoordinatorLayout;
    @BindString(R.string.alert_connectivity_status_ok)          String mConnectivityOk;
    @BindString(R.string.alert_connectivity_status_notok)       String mConnectivityNotOk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mRecipeListFragment = (RecipeListFragment) getFragmentManager().findFragmentById(R.id.fragment_recipe_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseApplication.setReceiverStatus(true);
        BaseApplication.setConnectivityListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BaseApplication.setReceiverStatus(false);
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        showSnack(isConnected);
        if (isConnected) {
            mRecipeListFragment.loadRecipeData();
        }
    }


    /**
     * Method to display Snackbar with connectivity message
     * @param isConnected
     */
    public void showSnack(boolean isConnected) {
        String message;

        message = (isConnected) ? mConnectivityOk : mConnectivityNotOk;
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }


    /**
     * Inflate Settings Menu for Movie List
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_refresh:
                mRecipeListFragment.loadRecipeData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}