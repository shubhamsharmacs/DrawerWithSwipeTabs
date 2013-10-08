package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Photo implements Parcelable {

    @JsonProperty("imageurl")
    private String imageUrl;

    @JsonProperty("caption")
    private String caption;

    public Photo() {}

    private Photo(Parcel in) {
        imageUrl = in.readString();
        caption = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(imageUrl);
        out.writeString(caption);
    }

    public static final Parcelable.Creator<Photo> CREATOR
            = new Parcelable.Creator<Photo>() {

        @Override
        public Photo createFromParcel(Parcel parcel) {
            return new Photo(parcel);
        }

        @Override
        public Photo[] newArray(int i) {
            return new Photo[i];
        }
    };

}
