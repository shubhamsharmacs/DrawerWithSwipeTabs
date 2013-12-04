package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Ad implements Parcelable {

    @JsonProperty("ad_id")
    private long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("url")
    private String url;

    @JsonProperty("image")
    private String imageUrl;

    public Ad() {}

    private Ad(Parcel in) {
        id = in.readLong();
        title = in.readString();
        url = in.readString();
        imageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeLong(id);
        out.writeString(title);
        out.writeString(url);
        out.writeString(imageUrl);
    }
}
