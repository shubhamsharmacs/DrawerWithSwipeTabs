package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;

@ApiResource(Resource.REVIEW)
public class ReviewResult extends BaseResult<Review> {

    public ReviewResult() {}

    private ReviewResult(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<ReviewResult> CREATOR
            = new Parcelable.Creator<ReviewResult>() {

        @Override
        public ReviewResult createFromParcel(Parcel parcel) {
            return new ReviewResult(parcel);
        }

        @Override
        public ReviewResult[] newArray(int i) {
            return new ReviewResult[i];
        }
    };

}
