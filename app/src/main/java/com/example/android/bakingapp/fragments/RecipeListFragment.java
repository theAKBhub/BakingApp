package com.example.android.bakingapp.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.SimpleIdlingResource;
import com.example.android.bakingapp.activities.DetailActivity;
import com.example.android.bakingapp.activities.MainActivity;
import com.example.android.bakingapp.adapters.RecipeListAdapter;
import com.example.android.bakingapp.exceptions.NoConnectivityException;
import com.example.android.bakingapp.interfaces.RecipeInterface;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.network.RetrofitController;
import com.example.android.bakingapp.utils.Config;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by aditibhattacharya on 31/01/2018.
 */

public class RecipeListFragment extends Fragment implements RecipeListAdapter.RecipeListOnClickHandler {

    private MainActivity mParentActivity;
    private RecipeListAdapter mRecipeListAdapter;
    private ArrayList<Recipe> mRecipeList;
    private Unbinder mUnbinder;
    SimpleIdlingResource mSimpleIdlingResource;

    @BindView(R.id.recyclerview_recipes)                RecyclerView mRecyclerView;
    @BindView(R.id.progress_indicator)                  ProgressBar mLoadingIndicator;
    @BindView(R.id.textview_empty_list)                 TextView mTextViewEmptyList;
    @BindInt(R.integer.grid_column_count)               int mGridColumnCount;
    @BindString(R.string.alert_recipe_load_failure)     String mAlertRecipeLoadFailure;
    @BindString(R.string.error_recipe_load)             String mErrorRecipeLoad;


    /** Empty Constructor */
    public RecipeListFragment() {}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mParentActivity = (MainActivity) getActivity();

        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        // Initialize RecyclerView for displaying recipes using grid layout
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mParentActivity, mGridColumnCount);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        // Set Adapter
        mRecipeListAdapter = new RecipeListAdapter(this);
        mRecyclerView.setAdapter(mRecipeListAdapter);

        mRecipeListAdapter.setRecipeData(mRecipeList);
        mRecipeListAdapter.notifyDataSetChanged();

        mSimpleIdlingResource = (SimpleIdlingResource) mParentActivity.getIdlingResource();
        if (mSimpleIdlingResource != null) {
            mSimpleIdlingResource.setIdleState(false);
        }

        // Load recipes data
        loadRecipeData();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    /**
     * Method to load data into the adapter and display in the RecyclerView layout
     * Displays alert messages if
     * (1) There is no connectivity
     * (2) A failure happened while fetching/loading data
     */
    public void loadRecipeData() {
        try {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mTextViewEmptyList.setVisibility(View.GONE);

            RecipeInterface apiInterface = RetrofitController.
                    getClient(mParentActivity).
                    create(RecipeInterface.class);

            final Call<ArrayList<Recipe>> recipeListCall = apiInterface.getRecipe();

            recipeListCall.enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                    int statusCode = response.code();

                    if (response.isSuccessful()) {
                        postDataLoad(true, "");

                        mRecipeList = response.body();
                        MainActivity.sRecipeList = mRecipeList;
                        mRecipeListAdapter.setRecipeData(mRecipeList);
                        mRecipeListAdapter.notifyDataSetChanged();
                    } else {
                        postDataLoad(false, mAlertRecipeLoadFailure);
                        Timber.e(mErrorRecipeLoad, statusCode);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Recipe>> call, Throwable throwable) {
                    postDataLoad(false, mAlertRecipeLoadFailure);
                    Timber.e(throwable.getMessage());
                }
            });

        } catch (NoConnectivityException nce) {
            postDataLoad(false, mAlertRecipeLoadFailure);
            Timber.e(nce.getMessage());
        }

        if (mSimpleIdlingResource != null) {
            mSimpleIdlingResource.setIdleState(true);
        }
    }


    @Override
    public void onClick(Recipe recipe) {
        Bundle recipeBundle = new Bundle();
        ArrayList<Recipe> selectedRecipe = new ArrayList<>();
        selectedRecipe.add(recipe);
        recipeBundle.putParcelableArrayList(Config.INTENT_KEY_SELECTED_RECIPE, selectedRecipe);

        Intent intent = new Intent(mParentActivity, DetailActivity.class);
        intent.putExtras(recipeBundle);
        startActivity(intent);
    }


    public void postDataLoad(boolean isLoadSuccessful, String message) {
        if (mLoadingIndicator.getVisibility() == View.VISIBLE) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }

        if (isLoadSuccessful) {
            mTextViewEmptyList.setVisibility(View.GONE);
        } else {
            mTextViewEmptyList.setText(message);
            mTextViewEmptyList.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
