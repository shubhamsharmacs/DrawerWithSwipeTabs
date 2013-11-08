package com.arcasolutions.api.model;

import android.os.Parcel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper = false)
public class DealCategory extends BaseCategory {

    @JsonProperty("active_listings")
    private int activeItems;

    public DealCategory() {
    }

    private DealCategory(Parcel in) {
        super(in);
        activeItems = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        super.writeToParcel(out, flag);
        out.writeInt(activeItems);
    }

    public static final Creator<DealCategory> CREATOR
            = new Creator<DealCategory>() {

        @Override
        public DealCategory createFromParcel(Parcel parcel) {
            return new DealCategory(parcel);
        }

        @Override
        public DealCategory[] newArray(int i) {
            return new DealCategory[i];
        }
    };

}
