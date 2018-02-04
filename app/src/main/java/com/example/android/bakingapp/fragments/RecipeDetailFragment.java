package com.example.android.bakingapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.DetailActivity;
import com.example.android.bakingapp.models.Ingredients;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Steps;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

/**
 * Created by aditibhattacharya on 03/02/2018.
 */

public class RecipeDetailFragment extends Fragment {

    private DetailActivity mParentActivity;
    private Unbinder mUnbinder;
    private Recipe mSelectedRecipe;
    Bundle mRecipeBundle;
    private ArrayList<Recipe> mRecipe;

    /** Empty Constructor */
    public RecipeDetailFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mParentActivity = (DetailActivity) getActivity();

        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        mSelectedRecipe = DetailActivity.mRecipe.get(0);

        TextView tv1 = rootView.findViewById(R.id.tv_ingredients);
        TextView tv2 = rootView.findViewById(R.id.tv_steps);

        List<Ingredients> ingredients = mSelectedRecipe.getRecipeIngredients();
        List<Steps> steps = mSelectedRecipe.getRecipeSteps();

        String i = "Ingredients ...\n";
        String s = "Steps ...\n";

        if (ingredients != null) {
            Timber.d("Ingredients not null ...");
            for (Ingredients ing : ingredients) {
                i += ing.getIngredient() + " - " + ing.getIngredientQuantity() + " " + ing.getIngredientMeasure() + "\n";
            }
        } else {
            Timber.d("Ingredients NULL ...");
        }

        if (steps != null) { Timber.d("Steps not null ...");
            for (Steps step : steps) {
                s += "(" + step.getStepId() + ") " + step.getStepShortDescription() + "\n";
            }
        } else {
            Timber.d("Steps NULL ...");
        }

        tv1.setText(i);
        tv2.setText(s);

        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
