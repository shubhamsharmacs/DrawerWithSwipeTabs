package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;

@ApiResource(Resource.LISTING_CATEGORY)
public class ListingCategoryResult extends BaseCategoryResult<ListingCategory> {

    public ListingCategoryResult() {
    }

    private ListingCategoryResult(Parcel in) {
        super(in);
    }

    public static final Creator<ListingCategoryResult> CREATOR
            = new Creator<ListingCategoryResult>() {

        @Override
        public ListingCategoryResult createFromParcel(Parcel parcel) {
            return new ListingCategoryResult(parcel);
        }

        @Override
        public ListingCategoryResult[] newArray(int i) {
            return new ListingCategoryResult[i];
        }
    };

}
