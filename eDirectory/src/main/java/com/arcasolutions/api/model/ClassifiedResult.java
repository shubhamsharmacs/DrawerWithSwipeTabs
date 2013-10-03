package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;

@ApiResource(Resource.CLASSIFIED)
public class ClassifiedResult extends BaseResult<Classified> {


    public ClassifiedResult() {}

    private ClassifiedResult(Parcel in) {
        super(in);
    }

    public static final Creator<ClassifiedResult> CREATOR
            = new Creator<ClassifiedResult>() {

        @Override
        public ClassifiedResult createFromParcel(Parcel parcel) {
            return new ClassifiedResult(parcel);
        }

        @Override
        public ClassifiedResult[] newArray(int i) {
            return new ClassifiedResult[i];
        }
    };

}
