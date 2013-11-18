package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.OrderBy;
import com.arcasolutions.api.constant.Resource;
import com.google.common.collect.Maps;

import java.util.Map;

@ApiResource(Resource.ARTICLE)
public class ArticleResult extends BaseResult<Article> {


    public ArticleResult() {
    }

    private ArticleResult(Parcel in) {
        super(in);
    }

    public static final Creator<ArticleResult> CREATOR
            = new Creator<ArticleResult>() {

        @Override
        public ArticleResult createFromParcel(Parcel parcel) {
            return new ArticleResult(parcel);
        }

        @Override
        public ArticleResult[] newArray(int i) {
            return new ArticleResult[i];
        }
    };

    @Override
    public Map<String, String> getOrderMap(OrderBy orderBy) {
        if (orderBy == null) throw new IllegalArgumentException("Article orderBy can not be null.");

        Map<String, String> order = Maps.newHashMap();

        switch (orderBy) {
            case PUBLICATION_DATE:
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

            default:
                throw new IllegalArgumentException("Unknown Article orderBy " + orderBy.toString());

        }
    }

}
