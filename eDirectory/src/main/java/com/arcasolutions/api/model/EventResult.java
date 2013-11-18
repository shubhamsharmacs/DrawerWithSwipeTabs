package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.OrderBy;
import com.arcasolutions.api.constant.Resource;
import com.google.common.collect.Maps;

import java.util.Map;

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

    @Override
    public Map<String, String> getOrderMap(OrderBy orderBy) {
        if (orderBy == null) throw new IllegalArgumentException("Event orderBy can not be null.");

        Map<String, String> order = Maps.newHashMap();

        switch (orderBy) {
            case LEVEL:
                order.put("level", "asc");
                return order;

            case START_DATE:
                order.put("", "");
                return order;

            case ALPHABETICALLY:
                order.put("name", "asc");
                return order;

            case POPULAR:
                order.put("number_views", "desc");
                return order;

            case DISTANCE:
                order.put("distance_score", "asc");
                return order;

            default:
                throw new IllegalArgumentException("Unknown Event orderBy " + orderBy.toString());

        }
    }

}
