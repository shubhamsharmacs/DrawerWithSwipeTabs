package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;

@ApiResource(Resource.EVENT_CATEGORY)
public class EventCategoryResult extends BaseCategoryResult<EventCategory> {

    public EventCategoryResult() {}

    private EventCategoryResult(Parcel in) {
        super(in);
    }

    public static final Creator<EventCategoryResult> CREATOR
            = new Creator<EventCategoryResult>() {

        @Override
        public EventCategoryResult createFromParcel(Parcel parcel) {
            return new EventCategoryResult(parcel);
        }

        @Override
        public EventCategoryResult[] newArray(int i) {
            return new EventCategoryResult[i];
        }
    };

}
