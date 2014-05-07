package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.arcasolutions.api.annotation.ApiModule;
import com.arcasolutions.api.constant.ModuleName;
import com.arcasolutions.api.deserializer.AddressDeserializer;
import com.arcasolutions.api.deserializer.BooleanDeserializer;
import com.arcasolutions.api.deserializer.SimpleDateDeserializer;
import com.arcasolutions.api.deserializer.SimpleTimeDeserializer;
import com.arcasolutions.api.implementation.IContactInfo;
import com.arcasolutions.api.implementation.IGeoPoint;
import com.arcasolutions.database.Database;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Maps;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModule(ModuleName.EVENT)
@DatabaseTable(tableName = Database.Tables.EVENTS)
public class Event extends Module implements IContactInfo, IGeoPoint, Comparable<Event> {

    @JsonProperty(value = "event_ID")
    @DatabaseField(id = true, columnName = Database.EventsColumns.EVENT_ID)
    private long id;

    @JsonProperty("name")
    @DatabaseField(columnName = Database.EventsColumns.EVENT_TITLE)
    private String title;

    @JsonProperty("address")
    @JsonDeserialize(using = AddressDeserializer.class)
    @DatabaseField(columnName = Database.EventsColumns.EVENT_ADDRESS)
    private String address;

    @JsonProperty("imageurl")
    @DatabaseField(columnName = Database.EventsColumns.EVENT_ICON)
    private String imageUrl;

    private float rating = -1;

    @JsonProperty("phonenumber")
    private String phoneNumber;

    @JsonProperty("latitude")
    @DatabaseField(columnName = Database.EventsColumns.EVENT_LATITUDE)
    private double latitude;

    @JsonProperty("longitude")
    @DatabaseField(columnName = Database.EventsColumns.EVENT_LONGITUDE)
    private double longitude;

    @JsonProperty("start_date")
    @JsonDeserialize(using = SimpleDateDeserializer.class)
    @DatabaseField(columnName = Database.EventsColumns.EVENT_START_DATE, dataType = DataType.DATE_LONG)
    private Date startDate;

    @JsonProperty("end_date")
    @JsonDeserialize(using = SimpleDateDeserializer.class)
    private Date endDate;

    @JsonProperty("start_time")
    @JsonDeserialize(using = SimpleTimeDeserializer.class)
    private Date startTime;

    @JsonProperty("end_time")
    @JsonDeserialize(using = SimpleTimeDeserializer.class)
    private Date endTime;

    @JsonProperty("recurring")
    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean recurring;

    @JsonProperty("until_date")
    @JsonDeserialize(using = SimpleDateDeserializer.class)
    private Date untilDate;

    @JsonProperty("repeat_event")
    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean repeatEvent;

    @JsonProperty("recurring_string")
    private String recurringString;

    @JsonProperty("string_time")
    private String stringTime;

    @JsonProperty("description")
    private String summary;

    @JsonProperty("long_description")
    private String description;

    @JsonProperty
    private String url;

    @JsonProperty
    private String email;

    @JsonProperty
    private int level;

    @JsonProperty("friendly_url")
    private String friendlyUrl;

    @JsonProperty("gallery")
    private ArrayList<Photo> gallery;

    @JsonProperty("location_information")
    @JsonDeserialize(using = AddressDeserializer.class)
    private String locationInformation;

    @Override
    public Map<String, String> getLevelFieldsMap() {
        Map<String, String> map = Maps.newHashMap();
        map.put("summary_description", "summary");
        map.put("email", "email");
        map.put("url", "url");
//        map.put("contact_name", "");
        map.put("end_time", "endTime");
        map.put("start_time", "startTime");
        map.put("long_description", "description");
        map.put("main_image", "imageUrl");
        map.put("phone", "phoneNumber");
//        map.put("video_snippet", "");
//        map.put("video", "");
        return map;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Event() {
    }

    public String getAddress() {
        return TextUtils.isEmpty(locationInformation)
                ? address
                : locationInformation;
    }

    private Event(Parcel in) {
        id = in.readLong();
        title = in.readString();
        address = in.readString();
        imageUrl = in.readString();
        rating = in.readFloat();
        phoneNumber = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        startDate = new Date(in.readLong());
        endDate = new Date(in.readLong());
        startTime = new Date(in.readLong());
        endTime = new Date(in.readLong());
        recurring = Boolean.getBoolean(in.readString());
        untilDate = new Date(in.readLong());
        repeatEvent = Boolean.getBoolean(in.readString());
        recurringString = in.readString();
        stringTime = in.readString();
        summary = in.readString();
        description = in.readString();
        url = in.readString();
        email = in.readString();
        level = in.readInt();
        friendlyUrl = in.readString();
        gallery = in.readArrayList(Photo.class.getClassLoader());
    }


    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeLong(id);
        out.writeString(title);
        out.writeString(address);
        out.writeString(imageUrl);
        out.writeFloat(rating);
        out.writeString(phoneNumber);
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        out.writeLong(startDate != null ? startDate.getTime() : 0);
        out.writeLong(endDate != null ? endDate.getTime() : 0);
        out.writeLong(startTime != null ? startTime.getTime() : 0);
        out.writeLong(endTime != null ? endTime.getTime() : 0);
        out.writeString(Boolean.toString(recurring));
        out.writeLong(untilDate != null ? untilDate.getTime() : 0);
        out.writeString(Boolean.toString(repeatEvent));
        out.writeString(recurringString);
        out.writeString(stringTime);
        out.writeString(summary);
        out.writeString(description);
        out.writeString(url);
        out.writeString(email);
        out.writeInt(level);
        out.writeString(friendlyUrl);
        out.writeList(gallery);
    }

    public static final Parcelable.Creator<Event> CREATOR
            = new Parcelable.Creator<Event>() {

        @Override
        public Event createFromParcel(Parcel parcel) {
            return new Event(parcel);
        }

        @Override
        public Event[] newArray(int i) {
            return new Event[i];
        }
    };

    @Override
    public int compareTo(Event event) {
        if (getStartDate() == null || event.getStartDate() == null ) return 0;
        return getStartDate().compareTo(event.getStartDate());
    }

    @Override
    public String getImageUrlCat() {
        return null;
    }
}
