package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;

public class BaseCategoryResult<T extends BaseCategory> extends BaseResult<T> {

    public BaseCategoryResult() {}

    protected BaseCategoryResult(Parcel in) {
        super(in);
    }

}
