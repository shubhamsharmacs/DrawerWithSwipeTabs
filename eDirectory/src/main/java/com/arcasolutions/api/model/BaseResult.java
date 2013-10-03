package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.android.gms.internal.o;

import lombok.Data;

@Data
public abstract class BaseResult<T> implements Parcelable {

    @JsonProperty("type")
    private String type;

    @JsonProperty("total_results")
    private int totalResults;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("results_per_page")
    private int resultsPerPage;

    @JsonProperty("results")
    private java.util.List<T> results;

    public BaseResult() {}

    protected BaseResult(Parcel in) {
        type = in.readString();
        totalResults = in.readInt();
        totalPages = in.readInt();
        resultsPerPage = in.readInt();
        results = in.readArrayList(ClassLoader.getSystemClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(type);
        out.writeInt(totalResults);
        out.writeInt(totalPages);
        out.writeInt(resultsPerPage);

        if (results != null) {
            Object[] results = new Object[this.results.size()];
            this.results.toArray(results);
            out.writeArray(results);
        } else {
            out.writeArray(null);
        }
    }
}
