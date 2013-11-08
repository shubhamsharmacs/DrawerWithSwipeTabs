package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.implementation.IContactInfo;
import com.arcasolutions.api.implementation.IGeoPoint;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper = false)
public class Classified extends Module implements IGeoPoint, IContactInfo {

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

    @Override
    public Map<String, String> getLevelFieldsMap() {
        Map<String, String> map = Maps.newHashMap();
//        map.put("contact_name", "");
//        map.put("fax", "");
        map.put("url", "url");
        map.put("long_description", "description");
        map.put("main_image", "imageUrl");
        map.put("summary_description", "summary");
        map.put("contact_phone", "phone");
        map.put("contact_email", "email");
        map.put("price", "price");
        return map;
    }

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

    public float getRating() {
        return 0;
    }

    public String getTitle() {
        return getName();
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
