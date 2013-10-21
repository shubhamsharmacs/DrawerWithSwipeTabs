package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.implementation.IContactInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Classified extends Module implements IContactInfo {

    @JsonProperty("classified_ID")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("location_information")
    private String address;

    @JsonProperty("imageurl")
    private String imageUrl;

    @JsonProperty("phonenumber")
    private String phoneNumber;

    @JsonProperty("latitude")
    private double latitude;

    @JsonProperty("longitude")
    private double longitude;

    @JsonProperty("price")
    private float price;

    @JsonProperty("summarydesc")
    private String summary;

    @JsonProperty("detaildesc")
    private String description;

    @JsonProperty("friendly_url")
    private String friendlyUrl;

    @JsonProperty("gallery")
    private ArrayList<Photo> gallery;

    @JsonProperty
    private String url;

    @JsonProperty
    private String email;

    @JsonProperty
    private int level;


    public Classified() {
    }

    private Classified(Parcel in) {
        id = in.readLong();
        name = in.readString();
        address = in.readString();
        imageUrl = in.readString();
        phoneNumber = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        price = in.readFloat();
        summary = in.readString();
        description = in.readString();
        friendlyUrl = in.readString();
        gallery = in.readArrayList(Photo.class.getClassLoader());
        url = in.readString();
        email = in.readString();
        level = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        out.writeLong(id);
        out.writeString(name);
        out.writeString(address);
        out.writeString(imageUrl);
        out.writeString(phoneNumber);
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        out.writeFloat(price);
        out.writeString(summary);
        out.writeString(description);
        out.writeString(friendlyUrl);
        out.writeList(gallery);
        out.writeString(url);
        out.writeString(email);
        out.writeInt(level);
    }

    public static final Creator<Classified> CREATOR
            = new Creator<Classified>() {

        @Override
        public Classified createFromParcel(Parcel parcel) {
            return new Classified(parcel);
        }

        @Override
        public Classified[] newArray(int i) {
            return new Classified[i];
        }
    };
}
