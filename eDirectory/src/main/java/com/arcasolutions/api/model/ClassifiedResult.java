package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.OrderBy;
import com.arcasolutions.api.constant.Resource;
import com.google.common.collect.Maps;

import java.util.Map;

@ApiResource(Resource.CLASSIFIED)
public class ClassifiedResult extends BaseResult<Classified> {


    public ClassifiedResult() {
    }

    private ClassifiedResult(Parcel in) {
        super(in);
    }

    public static final Creator<ClassifiedResult> CREATOR
            = new Creator<ClassifiedResult>() {

        @Override
        public ClassifiedResult createFromParcel(Parcel parcel) {
            return new ClassifiedResult(parcel);
        }

        @Override
        public ClassifiedResult[] newArray(int i) {
            return new ClassifiedResult[i];
        }
    };

    @Override
    public Map<String, String> getOrderMap(OrderBy orderBy) {
        if (orderBy == null) throw new IllegalArgumentException("Classified orderBy can not be null.");

        Map<String, String> order = Maps.newHashMap();

        switch (orderBy) {
            case LEVEL:
                order.put("", "");
                return order;

            case ALPHABETICALLY:
                order.put("name", "asc");
                return order;

            case LAST_UPDATED:
                order.put("updated", "desc");
                return order;

            case POPULAR:
                order.put("number_views", "desc");
                return order;

            case DISTANCE:
                order.put("distance_score", "asc");
                return order;

            default:
                throw new IllegalArgumentException("Unknown Classified orderBy " + orderBy.toString());

        }
    }

}
