package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.api.deserializer.BooleanDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Maps;

import java.util.Map;

import lombok.Data;

@Data
public class EdirectoryConf implements Parcelable {

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("distance_label")
    private String distanceLabel;

    @JsonProperty("format_date")
    private String dateFormat;

    @JsonProperty("edir_reviews")
    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean review;

    @JsonProperty("review_approve")
    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean reviewApprove;

    @JsonProperty("review_article_enabled")
    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean reviewArticle;

    @JsonProperty("review_listing_enabled")
    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean reviewListing;

    @JsonProperty("review_promotion_enabled")
    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean reviewDeal;

    @JsonProperty("review_manditory")
    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean reviewManditory;

    @JsonProperty("app_api_version")
    private String apiVersionName;

    @JsonProperty("listing_price_info")
    private Map<String, String> priceInfoMap;

    public static interface API_VERSIONS {
        public int V1 = 1;
        public int V1_1 = 2;
        public int V1_2 = 3;
        public int V1_3 = 4;
    }

    public int getApiVersion() {
        if  ("1.3".equalsIgnoreCase(apiVersionName)) return API_VERSIONS.V1_3;
        if  ("1.2".equalsIgnoreCase(apiVersionName)) return API_VERSIONS.V1_2;
        if  ("1.1".equalsIgnoreCase(apiVersionName)) return API_VERSIONS.V1_1;
        else return API_VERSIONS.V1;
    }

    public EdirectoryConf() {}

    private EdirectoryConf(Parcel in) {
        currency = in.readString();
        distanceLabel = in.readString();
        dateFormat = in.readString();
        review = in.readByte() == 1;
        reviewApprove = in.readByte() == 1;
        reviewArticle = in.readByte() == 1;
        reviewListing = in.readByte() == 1;
        reviewDeal = in.readByte() == 1;
        reviewManditory = in.readByte() == 1;
        apiVersionName = in.readString();
        int size = in.readInt();
        priceInfoMap = Maps.newHashMap();
        for (int i=0; i<size; i++) {
            String key = in.readString();
            String value = in.readString();
            priceInfoMap.put(key, value);
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(currency);
        out.writeString(distanceLabel);
        out.writeString(dateFormat);
        out.writeByte((byte) (review ? 1 : 0));
        out.writeByte((byte) (reviewApprove ? 1 : 0));
        out.writeByte((byte) (reviewArticle ? 1 : 0));
        out.writeByte((byte) (reviewListing ? 1 : 0));
        out.writeByte((byte) (reviewDeal ? 1 : 0));
        out.writeByte((byte) (reviewManditory ? 1 : 0));
        out.writeString(apiVersionName);
        out.writeInt(priceInfoMap != null ? priceInfoMap.size() : 0);
        if (priceInfoMap != null) {
            for (Map.Entry<String, String> entry : priceInfoMap.entrySet()) {
                out.writeString(entry.getKey());
                out.writeString(entry.getValue());
            }
        }
    }
}
