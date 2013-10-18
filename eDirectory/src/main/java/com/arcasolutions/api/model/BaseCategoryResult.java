package com.arcasolutions.api.model;

import android.os.Parcel;

public class BaseCategoryResult<T extends BaseCategory> extends BaseResult<T> {

    public BaseCategoryResult() {
    }

    protected BaseCategoryResult(Parcel in) {
        super(in);
    }

}
