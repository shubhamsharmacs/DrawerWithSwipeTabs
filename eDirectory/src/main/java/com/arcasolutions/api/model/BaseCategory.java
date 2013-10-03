package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public abstract class BaseCategory implements Parcelable {

    @JsonProperty("category_id")
    private long id;

    @JsonProperty("father_id")
    private long categoryId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("image")
    private String imageUrl;

    @JsonProperty("total_sub")
    private int totalSubs;

    public abstract int getActiveItems();

    public BaseCategory(){}

    protected BaseCategory(Parcel in) {
        id = in.readLong();
        categoryId = in.readLong();
        name = in.readString();
        imageUrl = in.readString();
        totalSubs = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        out.writeLong(id);
        out.writeLong(categoryId);
        out.writeString(name);
        out.writeString(imageUrl);
        out.writeInt(totalSubs);
    }

}
