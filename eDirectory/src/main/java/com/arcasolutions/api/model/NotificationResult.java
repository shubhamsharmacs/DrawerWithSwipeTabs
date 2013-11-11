package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;

@ApiResource(Resource.NOTIFICATION)
public class NotificationResult extends BaseResult<Notification> {


    public NotificationResult() {
    }

    private NotificationResult(Parcel in) {
        super(in);
    }

    public static final Creator<NotificationResult> CREATOR
            = new Creator<NotificationResult>() {

        @Override
        public NotificationResult createFromParcel(Parcel parcel) {
            return new NotificationResult(parcel);
        }

        @Override
        public NotificationResult[] newArray(int i) {
            return new NotificationResult[i];
        }
    };

}
