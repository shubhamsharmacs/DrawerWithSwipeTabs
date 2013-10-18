package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.api.deserializer.DateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

import lombok.Data;

@Data
public class Review extends Module {

    @JsonProperty("review_id")
    private long id;

    @JsonProperty("added")
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private Date dateTime;

    @JsonProperty("review_title")
    private String title;

    @JsonProperty("review")
    private String content;

    @JsonProperty("reviewer_name")
    private String reviewerName;

    @JsonProperty("reviewer_email")
    private String reviewerEmail;

    @JsonProperty("reviewer_location")
    private String reviewerLocation;

    @JsonProperty("member_img")
    private String reviewerAvatarUrl;

    @JsonProperty("rating")
    private float rating;

    public Review() {}

    private Review(Parcel in) {
        id = in.readLong();
        dateTime = new Date(in.readLong());
        title = in.readString();
        content = in.readString();
        reviewerName = in.readString();
        reviewerEmail = in.readString();
        reviewerLocation = in.readString();
        reviewerAvatarUrl = in.readString();
        rating = in.readFloat();
    }

    public static final Parcelable.Creator<Review> CREATOR
            = new Parcelable.Creator<Review>() {

        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeLong(id);
        out.writeLong(dateTime != null ? dateTime.getTime() : 0);
        out.writeString(title);
        out.writeString(content);
        out.writeString(reviewerName);
        out.writeString(reviewerEmail);
        out.writeString(reviewerLocation);
        out.writeString(reviewerAvatarUrl);
        out.writeFloat(rating);
    }
}
