package com.arcasolutions.api.model;

import android.os.Parcel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClassifiedCategory extends BaseCategory {

    @JsonProperty("active_classifieds")
    private int activeItems;

    public ClassifiedCategory() {
    }

    private ClassifiedCategory(Parcel in) {
        super(in);
        activeItems = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        super.writeToParcel(out, flag);
        out.writeInt(activeItems);
    }

    public static final Creator<ClassifiedCategory> CREATOR
            = new Creator<ClassifiedCategory>() {

        @Override
        public ClassifiedCategory createFromParcel(Parcel parcel) {
            return new ClassifiedCategory(parcel);
        }

        @Override
        public ClassifiedCategory[] newArray(int i) {
            return new ClassifiedCategory[i];
        }
    };

}
