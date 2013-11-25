package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper = false)
@ApiResource(Resource.EVENT)
public class EventScheduleResult extends BaseResult<EventSchedule> {

    @JsonProperty("results")
    private EventSchedule calendar;

    public EventScheduleResult() {
    }

    private EventScheduleResult(Parcel in) {
        super(in);
        calendar = (EventSchedule) in.readHashMap(ClassLoader.getSystemClassLoader());
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        super.writeToParcel(out, i);
        out.writeMap(calendar);
    }

    public static final Parcelable.Creator<EventScheduleResult> CREATOR
            = new Parcelable.Creator<EventScheduleResult>() {

        @Override
        public EventScheduleResult createFromParcel(Parcel parcel) {
            return new EventScheduleResult(parcel);
        }

        @Override
        public EventScheduleResult[] newArray(int i) {
            return new EventScheduleResult[i];
        }
    };


}
