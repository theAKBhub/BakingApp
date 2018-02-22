package com.example.android.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * A {@link Step} object that contains steps related to a single Recipe item
 * Created by aditibhattacharya on 26/01/2018.
 */

public class Step implements Parcelable {

    /**
     * {@link Step} Attributes
     * Each attribute has a corresponding @SerializedName that is needed for GSON
     * to map the JSON keys with the attributes of {@link Step} object.
     */

    @SerializedName("id")
    private Integer mStepId;

    @SerializedName("shortDescription")
    private String mStepShortDescription;

    @SerializedName("description")
    private String mStepDescription;

    @SerializedName("videoURL")
    private String mStepVideoUrl;

    @SerializedName("thumbnailURL")
    private String mStepThumbnailURL;

    // This attribute is used to identify current step selection and change background color accordingly
    private boolean mIsSelected;

    public Step() {}

    /**
     * Getter and Setter methods for class Step
     */
    public Integer getStepId() {
        return mStepId;
    }

    public void setStepId(Integer stepId) {
        mStepId = stepId;
    }

    public String getStepShortDescription() {
        return mStepShortDescription;
    }

    public void setStepShortDescription(String shortDescription) {
        mStepShortDescription = shortDescription;
    }

    public String getStepDescription() {
        return mStepDescription;
    }

    public void setStepDescription(String description) {
        mStepDescription = description;
    }

    public String getStepVideoURL() {
        return mStepVideoUrl;
    }

    public void setStepVideoURL(String videoURL) {
        mStepVideoUrl = videoURL;
    }

    public String getStepThumbnailURL() {
        return mStepThumbnailURL;
    }

    public void setStepThumbnailURL(String thumbnailURL) {
        mStepThumbnailURL = thumbnailURL;
    }

    public boolean getIsSelected() {
        return mIsSelected;
    }

    public void setIsSelected(boolean selected) {
        mIsSelected = selected;
    }



    /**
     * Default Constructor - Constructs a new {@link Step} object
     * Scope for this constructor is private so CREATOR can access it
     * @param parcel
     */
    protected Step(Parcel parcel) {
        mStepId = parcel.readByte() == 0x00 ? null : parcel.readInt();
        mStepShortDescription = parcel.readString();
        mStepDescription = parcel.readString();
        mStepVideoUrl = parcel.readString();
        mStepThumbnailURL = parcel.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mStepId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mStepId);
        }
        dest.writeString(mStepShortDescription);
        dest.writeString(mStepDescription);
        dest.writeString(mStepVideoUrl);
        dest.writeString(mStepThumbnailURL);
    }


    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
