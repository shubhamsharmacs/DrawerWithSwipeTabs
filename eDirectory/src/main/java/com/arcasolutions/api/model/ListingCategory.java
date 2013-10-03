package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ListingCategory extends BaseCategory {

    @JsonProperty("active_listings")
    private int activeItems;

    public ListingCategory() {}

    private ListingCategory(Parcel in) {
        super(in);
        activeItems = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        super.writeToParcel(out, flag);
        out.writeInt(activeItems);
    }

    public static final Parcelable.Creator<ListingCategory> CREATOR
            = new Parcelable.Creator<ListingCategory>() {

        @Override
        public ListingCategory createFromParcel(Parcel parcel) {
            return new ListingCategory(parcel);
        }

        @Override
        public ListingCategory[] newArray(int i) {
            return new ListingCategory[i];
        }
    };

}
