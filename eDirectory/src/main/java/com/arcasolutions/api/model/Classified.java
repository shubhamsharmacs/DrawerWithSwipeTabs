package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.api.deserializer.SimpleDateDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

import lombok.Data;

@Data
public class Classified implements Parcelable {

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
