package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;

@ApiResource(Resource.EVENT)
public class EventResult extends BaseResult<Event> {

    public EventResult() {
    }

    private EventResult(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<EventResult> CREATOR
            = new Parcelable.Creator<EventResult>() {

        @Override
        public EventResult createFromParcel(Parcel parcel) {
            return new EventResult(parcel);
        }

        @Override
        public EventResult[] newArray(int i) {
            return new EventResult[i];
        }
    };

}
