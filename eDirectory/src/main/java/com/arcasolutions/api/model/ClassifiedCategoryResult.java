package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;

@ApiResource(Resource.CLASSIFIED_CATEGORY)
public class ClassifiedCategoryResult extends BaseCategoryResult<ClassifiedCategory> {

    public ClassifiedCategoryResult() {}

    private ClassifiedCategoryResult(Parcel in) {
        super(in);
    }

    public static final Creator<ClassifiedCategoryResult> CREATOR
            = new Creator<ClassifiedCategoryResult>() {

        @Override
        public ClassifiedCategoryResult createFromParcel(Parcel parcel) {
            return new ClassifiedCategoryResult(parcel);
        }

        @Override
        public ClassifiedCategoryResult[] newArray(int i) {
            return new ClassifiedCategoryResult[i];
        }
    };

}
