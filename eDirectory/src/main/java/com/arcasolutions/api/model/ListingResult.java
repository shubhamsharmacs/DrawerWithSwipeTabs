package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.OrderBy;
import com.arcasolutions.api.constant.Resource;
import com.google.common.collect.Maps;

import java.util.Map;

@ApiResource(Resource.LISTING)
public class ListingResult extends BaseResult<Listing> {


    public ListingResult() {
    }

    private ListingResult(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<ListingResult> CREATOR
            = new Parcelable.Creator<ListingResult>() {

        @Override
        public ListingResult createFromParcel(Parcel parcel) {
            return new ListingResult(parcel);
        }

        @Override
        public ListingResult[] newArray(int i) {
            return new ListingResult[i];
        }
    };


    @Override
    public Map<String, String> getOrderMap(OrderBy orderBy) {
        if (orderBy == null) throw new IllegalArgumentException("Listing orderBy can not be null.");

        Map<String, String> order = Maps.newHashMap();

        switch (orderBy) {
            case LEVEL:
                order.put("", "");
                return order;

            case ALPHABETICALLY:
                order.put("name", "asc");
                return order;

            case POPULAR:
                order.put("number_views", "desc");
                return order;

            case RATING:
                order.put("avg_review,level,name", "desc,asc,asc");
                return order;

            case DISTANCE:
                order.put("distance_score", "asc");
                return order;

            default:
                throw new IllegalArgumentException("Unknown Listing orderBy " + orderBy.toString());

        }
    }

}
