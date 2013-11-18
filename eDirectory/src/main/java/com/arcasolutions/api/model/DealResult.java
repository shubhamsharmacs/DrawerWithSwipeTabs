package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.OrderBy;
import com.arcasolutions.api.constant.Resource;
import com.google.common.collect.Maps;

import java.util.Map;

@ApiResource(Resource.DEAL)
public class DealResult extends BaseResult<Deal> {


    public DealResult() {
    }

    private DealResult(Parcel in) {
        super(in);
    }

    public static final Creator<DealResult> CREATOR
            = new Creator<DealResult>() {

        @Override
        public DealResult createFromParcel(Parcel parcel) {
            return new DealResult(parcel);
        }

        @Override
        public DealResult[] newArray(int i) {
            return new DealResult[i];
        }
    };

    @Override
    public Map<String, String> getOrderMap(OrderBy orderBy) {
        if (orderBy == null) throw new IllegalArgumentException("Deal orderBy can not be null.");

        Map<String, String> order = Maps.newHashMap();

        switch (orderBy) {
            case END_DATE:
                order.put("", "");
                return order;

            case ALPHABETICALLY:
                order.put("name", "asc");
                return order;

            case POPULAR:
                order.put("number_views", "desc");
                return order;

            case RATING:
                order.put("avg_review,name", "desc,asc");
                return order;

            case DISTANCE:
                order.put("distance_score", "asc");
                return order;

            default:
                throw new IllegalArgumentException("Unknown Deal orderBy " + orderBy.toString());

        }
    }

}
