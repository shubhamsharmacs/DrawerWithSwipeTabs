package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.api.deserializer.SimpleDateDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

import lombok.Data;

@Data
public class Article extends Module {

    @JsonProperty("article_ID")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("author")
    private String author;

    @JsonProperty("imageurl")
    private String imageUrl;

    @JsonProperty("publication_date")
    @JsonDeserialize(using = SimpleDateDeserializer.class)
    private Date pubDate;

    @JsonProperty("avg_review")
    private float rating;


    public Article() {
    }

    private Article(Parcel in) {
        id = in.readLong();
        name = in.readString();
        author = in.readString();
        imageUrl = in.readString();
        pubDate = new Date(in.readLong());
        rating = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        out.writeLong(id);
        out.writeString(name);
        out.writeString(author);
        out.writeString(imageUrl);
        out.writeLong(pubDate != null ? pubDate.getTime() : 0L);
        out.writeFloat(rating);
    }

    public static final Parcelable.Creator<Article> CREATOR
            = new Parcelable.Creator<Article>() {

        @Override
        public Article createFromParcel(Parcel parcel) {
            return new Article(parcel);
        }

        @Override
        public Article[] newArray(int i) {
            return new Article[i];
        }
    };
}
