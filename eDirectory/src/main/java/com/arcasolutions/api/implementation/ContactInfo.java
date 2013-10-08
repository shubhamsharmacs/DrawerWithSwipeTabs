package com.arcasolutions.api.implementation;

import android.os.Parcelable;

public interface ContactInfo extends Parcelable {

    public String getPhoneNumber();

    public String getEmail();

    public String getUrl();

}
