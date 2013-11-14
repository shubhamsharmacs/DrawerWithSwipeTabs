package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.api.deserializer.DateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

import lombok.Data;

@Data
public class CheckIn implements Parcelable {

    @JsonProperty("checkin_id")
    private long id;

    @JsonProperty("item_id")
    private long itemId;

    @JsonProperty("member_id")
    private long accountId;

    @JsonProperty("added")
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private Date added;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("quick_tip")
    private String quickTip;

    @JsonProperty("checkin_name")
    private String accountName;

    @JsonProperty("member_img")
    private String accountImageUrl;

    public CheckIn() {}

    private CheckIn(Parcel in) {
        id = in.readLong();
        itemId = in.readLong();
        accountId = in.readLong();
        added = new Date(in.readLong());
        ip = in.readString();
        quickTip = in.readString();
        accountName = in.readString();
        accountImageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeLong(id);
        out.writeLong(itemId);
        out.writeLong(accountId);
        out.writeLong(added != null ? added.getTime() : 0);
        out.writeString(ip);
        out.writeString(quickTip);
        out.writeString(accountName);
        out.writeString(accountImageUrl);
    }
}
