package com.example.android.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.example.android.bakingapp.utils.Config;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Recipe} object that contains details related to a single Recipe item
 * Created by aditibhattacharya on 26/01/2018.
 */

public class Recipe implements Parcelable {

    /**
     * {@link Recipe} Attributes
     * Each attribute has a corresponding @SerializedName that is needed for GSON
     * to map the JSON keys with the attributes of {@link Recipe} object.
     */

    // Recipe ID
    @SerializedName("id")
    private int mRecipeId;

    // Recipe Name
    @SerializedName("name")
    private String mRecipeName;

    // Recipe Ingredients
    @Nullable
    @SerializedName("ingredients")
    private List<Ingredients> mRecipeIngredients;

    // Recipe Steps
    @Nullable
    @SerializedName("steps")
    private List<Steps> mRecipeSteps;

    // Recipe Servings
    @Nullable
    @SerializedName("servings")
    private int mRecipeServings;

    // Recipe Image
    @Nullable
    @SerializedName("image")
    private String mRecipeImage;


    /**
     * Empty constructor
     */
    public Recipe() {
    }


    /** Getter method - Recipe ID */
    public int getRecipeId() {
        return mRecipeId;
    }

    /** Setter method - Recipe ID */
    public void setRecipeId(int recipeId) {
        mRecipeId = recipeId;
    }

    /** Getter method - Recipe Name */
    public String getRecipeName() {
        return mRecipeName;
    }

    /** Setter method - Recipe Name */
    public void setRecipeName(String recipeName) {
        mRecipeName = recipeName;
    }

    /** Getter method - Recipe Ingredients */
    @Nullable
    public List<Ingredients> getRecipeIngredients() {
        return mRecipeIngredients;
    }

    /** Setter method - Recipe Ingredients */
    public void setRecipeIngredients(@Nullable List<Ingredients> recipeIngredients) {
        mRecipeIngredients = recipeIngredients;
    }

    /** Getter method - Recipe Steps */
    @Nullable
    public List<Steps> getRecipeSteps() {
        return mRecipeSteps;
    }

    /** Setter method - Recipe Steps */
    public void setRecipeSteps(@Nullable List<Steps> recipeSteps) {
        mRecipeSteps = recipeSteps;
    }

    /** Getter method - Recipe Servings */
    @Nullable
    public int getRecipeServings() {
        return mRecipeServings;
    }

    /** Setter method - Recipe Servings */
    public void setRecipeServings(@Nullable int recipeServings) {
        mRecipeServings = recipeServings;
    }

    /** Getter method - Recipe Image */
    @Nullable
    public String getRecipeImage() {
        return mRecipeImage;
    }

    /** Setter method - Recipe Image */
    public void setRecipeImage(@Nullable String recipeImage) {
        mRecipeImage = recipeImage;
    }


    /**
     * Default Constructor - Constructs a new {@link Recipe} object
     * Scope for this constructor is private so CREATOR can access it
     * @param parcel
     */
    private Recipe(Parcel parcel) {
        mRecipeId = parcel.readInt();
        mRecipeName = parcel.readString();

        // Check if ingredients exist, then extract
        if (parcel.readByte() == Config.JSON_BYTE_VALUE) {
            mRecipeIngredients = new ArrayList<>();
            parcel.readList(mRecipeIngredients, Ingredients.class.getClassLoader());
        } else {
            mRecipeIngredients = null;
        }

        // Check if steps exist, then extract
        if (parcel.readByte() == Config.JSON_BYTE_VALUE) {
            mRecipeSteps = new ArrayList<>();
            parcel.readList(mRecipeSteps, Steps.class.getClassLoader());
        } else {
            mRecipeSteps = null;
        }

        // Check if servings value exists, then extract
        mRecipeServings = (parcel.readByte() == Config.JSON_BYTE_VALUE) ? parcel.readInt() : 0;

        // Check if image name exists, then extract
        mRecipeImage = (parcel.readByte() == Config.JSON_BYTE_VALUE) ? parcel.readString() : null;
    }


    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel inputParcel) {
            return new Recipe(inputParcel);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public void writeToParcel(Parcel outputParcel, int flags) {
        outputParcel.writeInt(mRecipeId);
        outputParcel.writeString(mRecipeName);
        outputParcel.writeList(mRecipeIngredients);
        outputParcel.writeList(mRecipeSteps);
        outputParcel.writeInt(mRecipeServings);
        outputParcel.writeString(mRecipeImage);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
