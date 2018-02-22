package com.example.android.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;
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

    @SerializedName("id")
    private Integer mRecipeId;

    @SerializedName("name")
    private String mRecipeName;

    @SerializedName("ingredients")
    private List<Ingredient> mRecipeIngredients = null;

    @SerializedName("steps")
    private List<Step> mRecipeSteps = null;

    @SerializedName("servings")
    private Integer mRecipeServings;

    @SerializedName("image")
    private String mRecipeImage;


    public Recipe() {}

    /**
     * Getter and Setter methods for class Recipe
     */

    public Integer getRecipeId() {
        return mRecipeId;
    }

    public void setRecipeId(Integer recipeId) {
        mRecipeId = recipeId;
    }

    public String getRecipeName() {
        return mRecipeName;
    }

    public void setRecipeName(String recipeName) {
        mRecipeName = recipeName;
    }

    public List<Ingredient> getRecipeIngredients() {
        return mRecipeIngredients;
    }

    public void setRecipeIngredients(List<Ingredient> ingredients) {
        mRecipeIngredients = ingredients;
    }

    public List<Step> getRecipeSteps() {
        return mRecipeSteps;
    }

    public void setRecipeSteps(List<Step> steps) {
        mRecipeSteps = steps;
    }

    public Integer getRecipeServings() {
        return mRecipeServings;
    }

    public void setRecipeServings(Integer servings) {
        mRecipeServings = servings;
    }

    public String getRecipeImage() {
        return mRecipeImage;
    }

    public void setRecipeImage(String image) {
        mRecipeImage = image;
    }



    /**
     * Default Constructor - Constructs a new {@link Recipe} object
     * Scope for this constructor is private so CREATOR can access it
     * @param parcel
     */
    public Recipe(Parcel parcel) {
        mRecipeId = parcel.readByte() == 0x00 ? null : parcel.readInt();
        mRecipeName = parcel.readString();
        if (parcel.readByte() == 0x01) {
            mRecipeIngredients = new ArrayList<>();
            parcel.readList(mRecipeIngredients, Ingredient.class.getClassLoader());
        } else {
            mRecipeIngredients = null;
        }
        if (parcel.readByte() == 0x01) {
            mRecipeSteps = new ArrayList<>();
            parcel.readList(mRecipeSteps, Step.class.getClassLoader());
        } else {
            mRecipeSteps = null;
        }
        mRecipeServings = parcel.readByte() == 0x00 ? null : parcel.readInt();
        mRecipeImage = parcel.readByte() == 0x00 ? null : parcel.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mRecipeId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mRecipeId);
        }

        dest.writeString(mRecipeName);

        if (mRecipeIngredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mRecipeIngredients);
        }

        if (mRecipeSteps == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mRecipeSteps);
        }

        if (mRecipeServings == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mRecipeServings);
        }

        dest.writeString(mRecipeImage);
    }


    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}