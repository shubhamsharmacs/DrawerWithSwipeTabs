package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;

@ApiResource(Resource.CHECKIN)
public class CheckInResult extends BaseResult<CheckIn> {

    public CheckInResult() {}

    private CheckInResult(Parcel in) {
        super(in);
    }

    public static final Creator<CheckInResult> CREATOR
            = new Creator<CheckInResult>() {

        @Override
        public CheckInResult createFromParcel(Parcel parcel) {
            return new CheckInResult(parcel);
        }

        @Override
        public CheckInResult[] newArray(int i) {
            return new CheckInResult[i];
        }
    };

}
