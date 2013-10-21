package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.api.deserializer.BooleanDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

import lombok.Data;

@Data
public class ModuleFeature implements Parcelable {

    @JsonProperty("level")
    private int level;

    @JsonProperty("detail")
    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean detail;

    @JsonProperty("images")
    private int imageCount;

    @JsonProperty("deal")
    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean deal;

    @JsonProperty("review")
    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean review;

    @JsonProperty("general")
    private List<String> general;

    public ModuleFeature() {}

    private ModuleFeature(Parcel in) {
        level = in.readInt();
        detail = in.readByte() == 1;
        imageCount = in.readInt();
        deal = in.readByte() == 1;
        review = in.readByte() == 1;
        general = in.readArrayList(String.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(level);
        out.writeByte((byte) (detail ? 1 : 0));
        out.writeInt(imageCount);
        out.writeByte((byte) (deal ? 1 : 0));
        out.writeByte((byte)(review ?  1 : 0));
        out.writeList(general);
    }
}
