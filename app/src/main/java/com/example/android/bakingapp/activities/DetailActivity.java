package com.example.android.bakingapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.RecipeDetailFragment;
import com.example.android.bakingapp.models.Ingredients;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.utils.Config;
import com.example.android.bakingapp.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

public class DetailActivity extends AppCompatActivity {

    public static ArrayList<Recipe> mRecipe;
    Bundle mRecipeBundle;
    static String SELECTED_RECIPES = "Selected_Recipes";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        // Only create new fragments when there is no previously saved state
        if (savedInstanceState == null) {

            if (getIntent().getExtras() != null) {
                mRecipeBundle = getIntent().getExtras();
                mRecipe = mRecipeBundle.getParcelableArrayList(Config.INTENT_EXTRA_SELECTED_RECIPE);
                Timber.d(mRecipe.get(0).getRecipeName() + " ... ");
            }

            // Exit early if selected recipe is a null object or has no recipe name
            if ((mRecipe == null) || (Utils.isEmptyString(mRecipe.get(0).getRecipeName()))) return;

            List<Ingredients> ingredients = mRecipe.get(0).getRecipeIngredients();
            Timber.d("Ingred count = " + ingredients.size());
            

            // Create an instance of FragmentManager
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Create fragment instance for ingredients
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(mRecipeBundle);
            fragmentManager.beginTransaction().add(R.id.container_recipe_detail, recipeDetailFragment).commit();

        }

    }
}
