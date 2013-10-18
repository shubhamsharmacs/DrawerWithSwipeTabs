package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;

@ApiResource(Resource.LISTING)
public class ListingResult extends BaseResult<Listing> {


    public ListingResult() {
    }

    private ListingResult(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<ListingResult> CREATOR
            = new Parcelable.Creator<ListingResult>() {

        @Override
        public ListingResult createFromParcel(Parcel parcel) {
            return new ListingResult(parcel);
        }

        @Override
        public ListingResult[] newArray(int i) {
            return new ListingResult[i];
        }
    };

}
