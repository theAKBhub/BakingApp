package com.example.android.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import butterknife.BindBool;
import butterknife.ButterKnife;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.RecipeDetailFragment;
import com.example.android.bakingapp.fragments.RecipeStepDetailFragment;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.utils.Config;
import com.example.android.bakingapp.utils.Utils;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnListItemClickListener {

    private static String STACK_RECIPE_DETAIL="STACK_RECIPE_DETAIL";
    private static String STACK_RECIPE_STEP_DETAIL="STACK_RECIPE_STEP_DETAIL";
    public static ArrayList<Recipe> sRecipe;
    private Bundle mRecipeBundle;
    private FragmentManager mFragmentManager;

    @BindBool(R.bool.two_pane_layout) boolean mIsTwoPaneLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            mRecipeBundle = getIntent().getExtras();
            sRecipe = mRecipeBundle.getParcelableArrayList(Config.INTENT_KEY_SELECTED_RECIPE);
        }

        // Exit early if selected recipe is a null object or has no recipe name
        if ((sRecipe == null) || (Utils.isEmptyString(sRecipe.get(0).getRecipeName()))) return;
        setTitle(sRecipe.get(0).getRecipeName());

        // Create an instance of FragmentManager
        mFragmentManager = getSupportFragmentManager();


        // Only create new fragments when there is no previously saved state
        if (savedInstanceState == null) {

            if (mIsTwoPaneLayout) {

                // Create fragment instance for ingredients and steps
                RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
                mRecipeBundle.putInt(Config.INTENT_KEY_SELECTED_STEP, 0);
                recipeDetailFragment.setArguments(mRecipeBundle);
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.container_recipe_detail, recipeDetailFragment)
                        .addToBackStack(STACK_RECIPE_DETAIL)
                        .commit();

                // Create fragment instance for Step Deatils
                RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
                mRecipeBundle.putInt(Config.INTENT_KEY_SELECTED_STEP, 0);
                recipeStepDetailFragment.setArguments(mRecipeBundle);
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.container_recipe_step_detail, recipeStepDetailFragment)
                        .addToBackStack(STACK_RECIPE_STEP_DETAIL)
                        .commit();

            } else {

                // Create fragment instance for ingredients and steps
                RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
                recipeDetailFragment.setArguments(mRecipeBundle);
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.container_recipe_detail, recipeDetailFragment)
                        .addToBackStack(STACK_RECIPE_DETAIL)
                        .commit();
            }
        }
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onItemSelected(int stepId) {

        int stepsCount = sRecipe.get(0).getRecipeSteps().size();

        if (mIsTwoPaneLayout) {
            Bundle stepBundle = new Bundle();
            stepBundle.putParcelableArrayList(Config.INTENT_KEY_SELECTED_RECIPE, sRecipe);
            stepBundle.putInt(Config.INTENT_KEY_SELECTED_STEP, stepId);
            stepBundle.putInt(Config.INTENT_KEY_STEP_COUNT, stepsCount);

            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(stepBundle);
            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.container_recipe_detail, recipeDetailFragment)
                    .commit();

            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setArguments(stepBundle);
            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.container_recipe_step_detail, recipeStepDetailFragment)
                    .commit();

        } else {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putExtra(Config.INTENT_KEY_SELECTED_STEP, stepId);
            intent.putExtra(Config.INTENT_KEY_STEP_COUNT, stepsCount);
            startActivity(intent);
        }
    }
}