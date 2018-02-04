package com.example.android.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.example.android.bakingapp.utils.Config;
import com.google.gson.annotations.SerializedName;

/**
 * A {@link Ingredient} object that contains ingredients related to a single Recipe item
 * Created by aditibhattacharya on 26/01/2018.
 */

public class Ingredient implements Parcelable {

    /**
     * {@link Ingredient} Attributes
     * Each attribute has a corresponding @SerializedName that is needed for GSON
     * to map the JSON keys with the attributes of {@link Ingredient} object.
     */

    // Ingredient Name
    @Nullable
    @SerializedName("ingredient")
    private String mIngredient;

    // Ingredient Quantity
    @Nullable
    @SerializedName("quantity")
    private double mIngredientQuantity;

    // Ingredient Quantity Measure
    @Nullable
    @SerializedName("measure")
    private String mIngredientMeasure;


    /**
     * Empty constructor
     */
    public Ingredient() {
    }


    /** Getter method - Ingredient Name */
    @Nullable
    public String getIngredient() {
        return mIngredient;
    }

    /** Setter method - Ingredient Name */
    public void setIngredient(String ingredient) {
        mIngredient = ingredient;
    }

    /** Getter method - Ingredient Quantity */
    @Nullable
    public double getIngredientQuantity() {
        return mIngredientQuantity;
    }

    /** Setter method - Ingredient Quantity */
    public void setIngredientQuantity(double ingredientQuantity) {
        mIngredientQuantity = ingredientQuantity;
    }

    /** Getter method - Ingredient Measure */
    @Nullable
    public String getIngredientMeasure() {
        return mIngredientMeasure;
    }

    /** Setter method - Ingredient Measure */
    public void setIngredientMeasure(String ingredientMeasure) {
        mIngredientMeasure = ingredientMeasure;
    }


    /**
     * Default Constructor - Constructs a new {@link Ingredient} object
     * Scope for this constructor is private so CREATOR can access it
     * @param parcel
     */
    private Ingredient(Parcel parcel) {
        // Check if ingredient quantity exists, then extract
        mIngredientQuantity = (parcel.readByte() == Config.JSON_BYTE_VALUE) ? parcel.readDouble() : 0;

        // Check if ingredient measure exists, then extract
        mIngredientMeasure = (parcel.readByte() == Config.JSON_BYTE_VALUE) ? parcel.readString() : null;

        // Check if ingredient name exists, then extract
        mIngredient = (parcel.readByte() == Config.JSON_BYTE_VALUE) ? parcel.readString() : null;
    }

    @Override
    public void writeToParcel(Parcel outputParcel, int flags) {
        outputParcel.writeDouble(mIngredientQuantity);
        outputParcel.writeString(mIngredientMeasure);
        outputParcel.writeString(mIngredient);
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel inputParcel) {
            return new Ingredient(inputParcel);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
