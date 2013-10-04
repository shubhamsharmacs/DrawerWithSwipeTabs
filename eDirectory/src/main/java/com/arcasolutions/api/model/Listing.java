package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;
import com.arcasolutions.api.implementation.IMapItem;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Listing  extends Module implements IMapItem {

    @JsonProperty("listing_ID")
    private long id;

    @JsonProperty("name")
    private String title;

    @JsonProperty("level")
    private int level;

    @JsonProperty("has_deal")
    private long dealId;

    @JsonProperty("location_information")
    private String address;

    @JsonProperty("rate")
    private float rating;

    @JsonProperty("imageurl")
    private String imageUrl;

    @JsonProperty("phonenumber")
    private String phoneNumber;

    @JsonProperty("latitude")
    private double latitude;

    @JsonProperty("longitude")
    private double longitude;

    @JsonProperty("description")
    private String description;

    @JsonProperty("total_reviews")
    private int totalReviews;

    public Listing(){}

    private Listing(Parcel in) {
        id = in.readLong();
        title = in.readString();
        level = in.readInt();
        dealId = in.readLong();
        address = in.readString();
        rating = in.readFloat();
        imageUrl = in.readString();
        phoneNumber = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        description = in.readString();
        totalReviews = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel in, int i) {
        in.writeLong(id);
        in.writeString(title);
        in.writeInt(level);
        in.writeLong(dealId);
        in.writeString(address);
        in.writeFloat(rating);
        in.writeString(imageUrl);
        in.writeString(phoneNumber);
        in.writeDouble(latitude);
        in.writeDouble(longitude);
        in.writeString(description);
        in.writeInt(totalReviews);
    }

    public static final Parcelable.Creator<Listing> CREATOR
            = new Parcelable.Creator<Listing>() {

        @Override
        public Listing createFromParcel(Parcel parcel) {
            return new Listing(parcel);
        }

        @Override
        public Listing[] newArray(int i) {
            return new Listing[i];
        }
    };
}
