package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;

@ApiResource(Resource.EDIRECTORY_CONF)
public class EdirectoryConfResult extends BaseResult<EdirectoryConf> {

    public EdirectoryConfResult() {}

    private EdirectoryConfResult(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<EdirectoryConfResult> CREATOR
            = new Parcelable.Creator<EdirectoryConfResult>() {

        @Override
        public EdirectoryConfResult createFromParcel(Parcel parcel) {
            return new EdirectoryConfResult(parcel);
        }

        @Override
        public EdirectoryConfResult[] newArray(int i) {
            return new EdirectoryConfResult[i];
        }
    };

}
