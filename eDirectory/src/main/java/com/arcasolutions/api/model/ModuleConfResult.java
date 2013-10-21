package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;

@ApiResource(Resource.MODULES_CONF)
public class ModuleConfResult extends BaseResult<ModuleConf> {

    public ModuleConfResult() {}

    private ModuleConfResult(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<ModuleConfResult> CREATOR
            = new Parcelable.Creator<ModuleConfResult>() {

        @Override
        public ModuleConfResult createFromParcel(Parcel parcel) {
            return new ModuleConfResult(parcel);
        }

        @Override
        public ModuleConfResult[] newArray(int i) {
            return new ModuleConfResult[i];
        }
    };

}
