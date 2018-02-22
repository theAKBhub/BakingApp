package com.example.android.bakingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindBool;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.DetailActivity;
import com.example.android.bakingapp.adapters.StepsAdapter;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;
import com.example.android.bakingapp.utils.Config;
import com.example.android.bakingapp.utils.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aditibhattacharya on 03/02/2018.
 */

public class RecipeDetailFragment extends Fragment implements StepsAdapter.StepsOnClickHandler {

    private DetailActivity mParentActivity;
    private Unbinder mUnbinder;
    private Recipe mSelectedRecipe;
    private StepsAdapter mStepsAdapter;
    private List<Ingredient> mIngredients;
    private List<Step> mSteps;
    public Integer mSelectedStepId;

    @BindView(R.id.recyclerview_steps)              RecyclerView mRecyclerViewSteps;
    @BindView(R.id.textview_ingredients)            TextView mTextViewIngredients;
    @BindBool(R.bool.two_pane_layout)               boolean mIsTwoPaneLayout;
    @BindString(R.string.display_ingredient)        String mDisplayIngredient;
    @BindString(R.string.error_missing_callback)    String mErrorMissingCallback;

    private OnListItemClickListener mCallBack;

    // OnViewClickListener interface, calls a method in the host activity named onItemSelected
    public interface OnListItemClickListener {
        void onItemSelected(int stepId);
    }


    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This ensures that the host activity has implemented the callback interface, else it throws an exception
        try {
            mCallBack = (OnListItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(String.format(mErrorMissingCallback, context.toString()));
        }
    }


    /** Empty Constructor */
    public RecipeDetailFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mParentActivity = (DetailActivity) getActivity();
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        if (getArguments() != null) {

            ArrayList<Recipe> recipes = getArguments().getParcelableArrayList(Config.INTENT_KEY_SELECTED_RECIPE);
            mSelectedStepId = getArguments().getInt(Config.INTENT_KEY_SELECTED_STEP);
            mSelectedRecipe = recipes.get(0);

            mIngredients = mSelectedRecipe.getRecipeIngredients();
            mSteps = mSelectedRecipe.getRecipeSteps();

            displayRecipeIngredients();
            displayRecipeSteps();
        }

        return rootView;
    }


    /**
     * Method to display Recipe Ingredients in a RecyclerView
     */
    public void displayRecipeIngredients() {

        StringBuilder ingredientDisplayString = new StringBuilder();

        for (Ingredient ingredient : mIngredients) {
            ingredientDisplayString.append(
                    String.format(
                            mDisplayIngredient,
                            Utils.convertStringToFirstCapital(ingredient.getIngredient()),
                            Double.toString(ingredient.getIngredientQuantity()),
                            ingredient.getIngredientMeasure().toLowerCase()
                    )
            );
        }

        mTextViewIngredients.setText(ingredientDisplayString.toString());
    }


    /**
     * Method to display Recipe Steps in a RecyclerView
     */
    public void displayRecipeSteps() {

        // Initialize RecyclerView for displaying recipe steps using LinearLayout
        RecyclerView.LayoutManager layoutManagerSteps = new LinearLayoutManager(mParentActivity);
        mRecyclerViewSteps.setLayoutManager(layoutManagerSteps);

        // Set Adapter
        mStepsAdapter = new StepsAdapter(mParentActivity, this);
        mRecyclerViewSteps.setAdapter(mStepsAdapter);
        mStepsAdapter.setStepsData(mSteps);

        if (mSelectedStepId == null) {
            mStepsAdapter.setSelected(0);
        } else {
            mStepsAdapter.setSelected(mSelectedStepId);
        }
    }


    @Override
    public void onClick(Step step) {
        mSelectedStepId = step.getStepId();
        mCallBack.onItemSelected(mSelectedStepId);
        mStepsAdapter.setSelected(mSelectedStepId);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
