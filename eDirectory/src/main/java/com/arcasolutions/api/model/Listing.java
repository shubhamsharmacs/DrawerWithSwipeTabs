package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.api.implementation.ContactInfo;
import com.arcasolutions.api.implementation.IMapItem;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Listing extends Module implements IMapItem, ContactInfo {

    @JsonProperty("listing_ID")
    private long id;

    @JsonProperty("name")
    private String title;

    @JsonProperty("level")
    private int level;

    @JsonProperty("has_deal")
    private long hasDeal;

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

    @JsonProperty("total_reviews")
    private int totalReviews;

    @JsonProperty("description")
    private String summary;

    @JsonProperty("long_description")
    private String description;

    @JsonProperty("video_snippet")
    private String videoSnippet;

    @JsonProperty("video_description")
    private String videoDescription;

    @JsonProperty("gallery")
    private ArrayList<Photo> gallery;

    @JsonProperty("total_checkins")
    private int totalCheckins;

    @JsonProperty("categories")
    private ArrayList<ListingCategory> categories;

    @JsonProperty("friendly_url")
    private String friendlyUrl;

    @JsonProperty("email")
    private String email;

    @JsonProperty("fax")
    private String fax;

    @JsonProperty("deal_id")
    private long dealId;

    @JsonProperty("deal_name")
    private String dealName;

    @JsonProperty("deal_price")
    private float dealPrice;

    @JsonProperty("deal_realvalue")
    private float dealRealPrice;

    @JsonProperty("deal_discount")
    private String dealDiscount;

    @JsonProperty("deal_remaining")
    private int dealRemaining;

    @JsonProperty("deal_description")
    private String dealDescription;

    @JsonProperty
    private String url;

    public Listing() {
    }

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

        summary = in.readString();
        description = in.readString();
        videoSnippet = in.readString();
        videoDescription = in.readString();
        gallery = in.readArrayList(ClassLoader.getSystemClassLoader());
        totalCheckins = in.readInt();
        categories = in.readArrayList(ClassLoader.getSystemClassLoader());
        friendlyUrl = in.readString();
        email = in.readString();
        fax = in.readString();
        dealId = in.readLong();
        dealName = in.readString();
        dealPrice = in.readFloat();
        dealRealPrice = in.readFloat();
        dealDiscount = in.readString();
        dealRemaining = in.readInt();
        dealDescription = in.readString();
        url = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeLong(id);
        out.writeString(title);
        out.writeInt(level);
        out.writeLong(dealId);
        out.writeString(address);
        out.writeFloat(rating);
        out.writeString(imageUrl);
        out.writeString(phoneNumber);
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        out.writeString(description);
        out.writeInt(totalReviews);
        out.writeString(summary);
        out.writeString(description);
        out.writeString(videoSnippet);
        out.writeString(videoDescription);
        out.writeList(gallery);
        out.writeInt(totalCheckins);
        out.writeList(categories);
        out.writeString(friendlyUrl);
        out.writeString(email);
        out.writeString(fax);
        out.writeLong(dealId);
        out.writeString(dealName);
        out.writeFloat(dealPrice);
        out.writeFloat(dealRealPrice);
        out.writeString(dealDiscount);
        out.writeInt(dealRemaining);
        out.writeString(dealDescription);
        out.writeString(url);
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
