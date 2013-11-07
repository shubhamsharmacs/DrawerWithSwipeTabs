package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;

@Data
@DatabaseTable(tableName = "account")
public class Account implements Parcelable {

    @DatabaseField(id = true)
    private long id;

    @DatabaseField
    private String uid;

    @DatabaseField
    private String email;

    @DatabaseField
    private String firstName;

    @DatabaseField
    private String lastName;

    public boolean isFacebook() {
        return TextUtils.isEmpty(uid);
    }

    public boolean isProfile() {
        return !isFacebook();
    }

    public String getFullName() {
        return TextUtils.join(" ", new String[]{firstName, lastName});
    }

    public Account() {}

    public Account(long id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Account(long id, String email, String firstName, String lastName, String uid) {
        this(id, email, firstName, lastName);
        this.uid = uid;
    }

    private Account(Parcel in) {
        id = in.readLong();
        uid = in.readString();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeLong(id);
        out.writeString(uid);
        out.writeString(email);
        out.writeString(firstName);
        out.writeString(lastName);
    }

    public static final Parcelable.Creator<Account> CREATOR
            = new Parcelable.Creator<Account>() {

        @Override
        public Account createFromParcel(Parcel parcel) {
            return new Account(parcel);
        }

        @Override
        public Account[] newArray(int i) {
            return new Account[i];
        }
    };
}