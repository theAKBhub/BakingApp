package com.example.android.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.example.android.bakingapp.utils.Config;
import com.google.gson.annotations.SerializedName;

/**
 * A {@link Steps} object that contains steps related to a single Recipe item
 * Created by aditibhattacharya on 26/01/2018.
 */

public class Steps implements Parcelable {

    /**
     * {@link Ingredients} Attributes
     * Each attribute has a corresponding @SerializedName that is needed for GSON
     * to map the JSON keys with the attributes of {@link Ingredients} object.
     */

    // Step ID
    @Nullable
    @SerializedName("id")
    private int mStepId;

    // Step short description
    @Nullable
    @SerializedName("shortDescription")
    private String mStepShortDescription;

    // Step description
    @Nullable
    @SerializedName("description")
    private String mStepDescription;

    // Step Video URL
    @Nullable
    @SerializedName("videoURL")
    private String mStepVideoUrl;

    // Step Thumbnail URL
    @Nullable
    @SerializedName("thumbnailURL")
    private String mStepThumbnailURL;


    /**
     * Empty constructor
     */
    public Steps() {
    }


    /** Getter method - Step ID */
    @Nullable
    public int getStepId() {
        return mStepId;
    }

    /** Setter method - Step ID */
    public void setStepId(@Nullable int stepId) {
        mStepId = stepId;
    }

    /** Getter method - Step short description */
    @Nullable
    public String getStepShortDescription() {
        return mStepShortDescription;
    }

    /** Setter method - Step short description */
    public void setStepShortDescription(@Nullable String stepShortDescription) {
        mStepShortDescription = stepShortDescription;
    }

    /** Getter method - Step description */
    @Nullable
    public String getStepDescription() {
        return mStepDescription;
    }

    /** Setter method - Step description */
    public void setStepDescription(@Nullable String stepDescription) {
        mStepDescription = stepDescription;
    }

    /** Getter method - Step video URL */
    @Nullable
    public String getStepVideoUrl() {
        return mStepVideoUrl;
    }

    /** Setter method - Step video URL */
    public void setStepVideoUrl(@Nullable String stepVideoUrl) {
        mStepVideoUrl = stepVideoUrl;
    }

    /** Getter method - Step thumbnail URL */
    @Nullable
    public String getStepThumbnailURL() {
        return mStepThumbnailURL;
    }

    /** Setter method - Step thumbnail URL */
    public void setStepThumbnailURL(@Nullable String stepThumbnailURL) {
        mStepThumbnailURL = stepThumbnailURL;
    }


    /**
     * Default Constructor - Constructs a new {@link Steps} object
     * Scope for this constructor is private so CREATOR can access it
     * @param parcel
     */
    private Steps(Parcel parcel) {
        // Check if Step ID exists, then extract
        mStepId = (parcel.readByte() == Config.JSON_BYTE_VALUE) ? parcel.readInt() : 0;

        // Check if Step short description exists, then extract
        mStepShortDescription = (parcel.readByte() == Config.JSON_BYTE_VALUE) ? parcel.readString() : null;

        // Check if Step description exists, then extract
        mStepDescription = (parcel.readByte() == Config.JSON_BYTE_VALUE) ? parcel.readString() : null;

        // Check if Step video URL exists, then extract
        mStepVideoUrl = (parcel.readByte() == Config.JSON_BYTE_VALUE) ? parcel.readString() : null;

        // Check if Step thumbnail URL exists, then extract
        mStepThumbnailURL = (parcel.readByte() == Config.JSON_BYTE_VALUE) ? parcel.readString() : null;
    }


    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel inputParcel) {
            return new Steps(inputParcel);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };

    @Override
    public void writeToParcel(Parcel outputParcel, int flags) {
        outputParcel.writeInt(mStepId);
        outputParcel.writeString(mStepShortDescription);
        outputParcel.writeString(mStepDescription);
        outputParcel.writeString(mStepVideoUrl);
        outputParcel.writeString(mStepThumbnailURL);
    }


    @Override
    public int describeContents() {
        return 0;
    }
}
