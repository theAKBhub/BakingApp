package com.example.android.bakingapp.interfaces;

import com.example.android.bakingapp.models.Recipe;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Purpose of this interface is to define the endpoints, that include details of
 * request methods (e.g. GET, POST, etc.) and parameters.
 * Created by aditibhattacharya on 27/01/2018.
 */

public interface RecipeInterface {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();
}
