package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.deserializer.SimpleDateDeserializer;
import com.arcasolutions.api.implementation.IGeoPoint;
import com.arcasolutions.database.Database;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@DatabaseTable(tableName = Database.Tables.DEALS)
public class Deal extends Module implements IGeoPoint {

    @JsonProperty("deal_ID")
    @DatabaseField(id = true, columnName = Database.DealsColumns.DEAL_ID)
    private long id;

    @JsonProperty("name")
    @DatabaseField(columnName = Database.DealsColumns.DEAL_TITLE)
    private String title;

    @JsonProperty("imageurl")
    @DatabaseField(columnName = Database.DealsColumns.DEAL_ICON)
    private String imageUrl;

    @JsonProperty("listing_id")
    private long listingId;

    @JsonProperty("listing_title")
    @DatabaseField(columnName = Database.DealsColumns.DEAL_LISTING_TITLE)
    private String listingTitle;

    @JsonProperty("listing_latitude")
    @DatabaseField(columnName = Database.DealsColumns.DEAL_LATITUDE)
    private double latitude;

    @JsonProperty("listing_longitude")
    @DatabaseField(columnName = Database.DealsColumns.DEAL_LONGITUDE)
    private double longitude;

    @JsonProperty("avg_review")
    @DatabaseField(columnName = Database.DealsColumns.DEAL_RATE)
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

    @JsonProperty("long_description")
    private String description;

    @JsonProperty("start_date")
    @JsonDeserialize(using = SimpleDateDeserializer.class)
    private Date startDate;

    @JsonProperty("end_date")
    @JsonDeserialize(using = SimpleDateDeserializer.class)
    private Date endDate;

    @JsonProperty("amount")
    private int amount;

    @JsonProperty("redeem_code")
    private String redeemCode;

    @JsonProperty("friendly_url")
    private String friendlyUrl;

    public int getLevel() {
        return 0;
    }

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
        description = in.readString();
        startDate = (Date) in.readSerializable();
        endDate = (Date) in.readSerializable();
        amount = in.readInt();
        redeemCode = in.readString();
        friendlyUrl = in.readString();
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
        out.writeString(description);
        out.writeSerializable(startDate);
        out.writeSerializable(endDate);
        out.writeInt(amount);
        out.writeString(redeemCode);
        out.writeString(friendlyUrl);
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

    @Override
    public String getImageUrlCat() {
        return null;
    }
}
