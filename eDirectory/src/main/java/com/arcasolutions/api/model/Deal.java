package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.implementation.IGeoPoint;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;

import java.util.Map;

import lombok.Data;

@Data
public class Deal extends Module implements IGeoPoint {

    @JsonProperty("deal_ID")
    private long id;

    @JsonProperty("name")
    private String title;

    @JsonProperty("imageurl")
    private String imageUrl;

    @JsonProperty("listing_id")
    private long listingId;

    @JsonProperty("listing_title")
    private String listingTitle;

    @JsonProperty("listing_latitude")
    private double latitude;

    @JsonProperty("listing_longitude")
    private double longitude;

    @JsonProperty("avg_review")
    private float rating;

    @JsonProperty("realvalue")
    private float realValue;

    @JsonProperty("dealvalue")
    private float dealValue;

    @JsonProperty("total_amount")
    private int totalAmount;

    @JsonProperty("conditions")
    private String conditions;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("total_reviews")
    private int totalReviews;

    @Override
    public Map<String, String> getLevelFieldsMap() {
        return null;
    }

    @Override
    public String getAddress() {
        return null;
    }

    public Deal() {
    }

    private Deal(Parcel in) {
        id = in.readLong();
        title = in.readString();
        imageUrl = in.readString();
        listingId = in.readLong();
        listingTitle = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        rating = in.readFloat();
        realValue = in.readFloat();
        dealValue = in.readFloat();
        totalAmount = in.readInt();
        conditions = in.readString();
        summary = in.readString();
        totalReviews = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeLong(id);
        out.writeString(title);
        out.writeString(imageUrl);
        out.writeLong(listingId);
        out.writeString(listingTitle);
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        out.writeFloat(rating);
        out.writeFloat(realValue);
        out.writeFloat(dealValue);
        out.writeInt(totalAmount);
        out.writeString(conditions);
        out.writeString(summary);
        out.writeInt(totalReviews);
    }

    public static final Creator<Deal> CREATOR
            = new Creator<Deal>() {

        @Override
        public Deal createFromParcel(Parcel parcel) {
            return new Deal(parcel);
        }

        @Override
        public Deal[] newArray(int i) {
            return new Deal[i];
        }
    };

}
