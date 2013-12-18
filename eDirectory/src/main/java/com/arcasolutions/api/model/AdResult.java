package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;

@ApiResource(Resource.AD)
public class AdResult extends BaseResult<Ad> {


    public AdResult() {
    }

    private AdResult(Parcel in) {
        super(in);
    }

    public static final Creator<AdResult> CREATOR
            = new Creator<AdResult>() {

        @Override
        public AdResult createFromParcel(Parcel parcel) {
            return new AdResult(parcel);
        }

        @Override
        public AdResult[] newArray(int i) {
            return new AdResult[i];
        }
    };

}
