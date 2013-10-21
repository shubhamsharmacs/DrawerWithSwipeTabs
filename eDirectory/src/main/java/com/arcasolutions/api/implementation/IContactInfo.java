package com.arcasolutions.api.implementation;

import android.os.Parcelable;

public interface IContactInfo extends Parcelable {

    public String getPhoneNumber();

    public String getEmail();

    public String getUrl();

}
