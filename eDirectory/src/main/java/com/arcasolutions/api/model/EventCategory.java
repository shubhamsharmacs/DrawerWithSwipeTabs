package com.arcasolutions.api.model;

import android.os.Parcel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper = false)
public class EventCategory extends BaseCategory {

    @JsonProperty("active_events")
    private int activeItems;

    public EventCategory() {
    }

    private EventCategory(Parcel in) {
        super(in);
        activeItems = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        super.writeToParcel(out, flag);
        out.writeInt(activeItems);
    }

    public static final Creator<EventCategory> CREATOR
            = new Creator<EventCategory>() {

        @Override
        public EventCategory createFromParcel(Parcel parcel) {
            return new EventCategory(parcel);
        }

        @Override
        public EventCategory[] newArray(int i) {
            return new EventCategory[i];
        }
    };

}
