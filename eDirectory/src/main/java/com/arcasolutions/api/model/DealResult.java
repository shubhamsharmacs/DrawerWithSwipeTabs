package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;

@ApiResource(Resource.DEAL)
public class DealResult extends BaseResult<Deal> {


    public DealResult() {}

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

}
