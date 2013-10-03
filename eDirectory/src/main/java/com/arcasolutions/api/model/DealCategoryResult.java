package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;

@ApiResource(Resource.DEAL_CATEGORY)
public class DealCategoryResult extends BaseCategoryResult<DealCategory> {

    public DealCategoryResult() {}

    private DealCategoryResult(Parcel in) {
        super(in);
    }

    public static final Creator<DealCategoryResult> CREATOR
            = new Creator<DealCategoryResult>() {

        @Override
        public DealCategoryResult createFromParcel(Parcel parcel) {
            return new DealCategoryResult(parcel);
        }

        @Override
        public DealCategoryResult[] newArray(int i) {
            return new DealCategoryResult[i];
        }
    };

}
