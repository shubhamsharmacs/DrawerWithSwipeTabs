package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.database.Database;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;

@Data
@DatabaseTable(tableName = Database.Tables.NOTIFICATIONS)
public class Notification implements Parcelable {

    @JsonProperty("notification_id")
    @DatabaseField(id = true, columnName = Database.NotificationsColumns.NOTIFICATION_ID)
    private long id;

    @JsonProperty("title")
    @DatabaseField(columnName = Database.NotificationsColumns.NOTIFICATION_TITLE)
    private String title;

    @JsonProperty("description")
    @DatabaseField(columnName = Database.NotificationsColumns.NOTIFICATION_DESCRIPTION)
    private String description;

    public Notification() {}

    public Notification(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeLong(id);
        out.writeString(title);
        out.writeString(description);
    }

    public static final Parcelable.Creator<Notification> CREATOR
            = new Parcelable.Creator<Notification>() {

        @Override
        public Notification createFromParcel(Parcel parcel) {
            return new Notification(parcel);
        }

        @Override
        public Notification[] newArray(int i) {
            return new Notification[i];
        }
    };

}
