package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.arcasolutions.api.annotation.ApiModule;
import com.arcasolutions.api.constant.ModuleName;
import com.arcasolutions.api.deserializer.AddressDeserializer;
import com.arcasolutions.api.implementation.IContactInfo;
import com.arcasolutions.api.implementation.IGeoPoint;
import com.arcasolutions.database.Database;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Maps;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModule(ModuleName.LISTING)
@DatabaseTable(tableName = Database.Tables.LISTINGS)
public class Listing extends Module implements IGeoPoint, IContactInfo {

    @JsonProperty("listing_ID")
    @DatabaseField(id = true, columnName = Database.ListingsColumns.LISTING_ID)
    private long id;

    @JsonProperty("name")
    @DatabaseField(columnName = Database.ListingsColumns.LISTING_TITLE)
    private String title;

    @JsonProperty("level")
    private int level;

    @JsonProperty("has_deal")
    @DatabaseField(columnName = Database.ListingsColumns.LISTING_HAS_DEAL)
    private long hasDeal;

    @JsonProperty("address")
    @JsonDeserialize(using = AddressDeserializer.class)
    @DatabaseField(columnName = Database.ListingsColumns.LISTING_ADDRESS)
    private String address;

    @JsonProperty("rate")
    @DatabaseField(columnName = Database.ListingsColumns.LISTING_RATE)
    private float rating;

    @JsonProperty("imageurl")
    @DatabaseField(columnName = Database.ListingsColumns.LISTING_ICON)
    private String imageUrl;

    @JsonProperty("imageurlcat")
    private String imageUrlCat;

    @JsonProperty("phonenumber")
    private String phoneNumber;

    @JsonProperty("latitude")
    @DatabaseField(columnName = Database.ListingsColumns.LISTING_LATITUDE)
    private double latitude;

    @JsonProperty("longitude")
    @DatabaseField(columnName = Database.ListingsColumns.LISTING_LONGITUDE)
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

    @JsonProperty("location_information")
    @JsonDeserialize(using = AddressDeserializer.class)
    private String locationInformation;

    @JsonProperty("color")
    @DatabaseField(columnName = Database.ListingsColumns.LISTING_LONGITUDE)
    private String color;

    @Override
    public String getListingId() {
        return null;
    }

    @Override
    public Map<String, String> getLevelFieldsMap() {
        Map<String, String> map = Maps.newHashMap();
        map.put("summary_description", "summary");
        map.put("email", "email");
        map.put("url", "url");
        map.put("fax", "fax");
//        map.put("video", "");
//        map.put("attachment_file", "");
        map.put("long_description", "description");
//        map.put("hours_of_work", "");
        map.put("locations", "address");
//        map.put("badges", "");
        map.put("main_image", "imageUrl");
        map.put("phone", "phoneNumber");
        return map;
    }

    public Listing() {
    }

    public String getAddress() {
        return TextUtils.isEmpty(locationInformation)
                ? address
                : locationInformation;
    }

    public String getImageUrlCat() {
        return imageUrlCat;
    }

    private Listing(Parcel in) {
        id = in.readLong();
        title = in.readString();
        level = in.readInt();
        dealId = in.readLong();
        address = in.readString();
        rating = in.readFloat();
        imageUrl = in.readString();
        imageUrlCat = in.readString();
        phoneNumber = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        description = in.readString();
        totalReviews = in.readInt();

        summary = in.readString();
        description = in.readString();
        videoSnippet = in.readString();
        videoDescription = in.readString();
        gallery = in.readArrayList(Photo.class.getClassLoader());
        totalCheckins = in.readInt();
        categories = in.readArrayList(ListingCategory.class.getClassLoader());
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
        color = in.readString();

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
        out.writeString(imageUrlCat);
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
        out.writeString(color);
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
