package com.arcasolutions.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.api.annotation.ApiModule;
import com.arcasolutions.api.constant.ModuleName;
import com.arcasolutions.api.deserializer.SimpleDateDeserializer;
import com.arcasolutions.database.Database;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModule(ModuleName.ARTICLE)
@DatabaseTable(tableName = Database.Tables.ARTICLES)
public class Article extends Module {

    @JsonProperty("article_ID")
    @DatabaseField(id = true, columnName = Database.ArticlesColumns.ARTICLE_ID)
    private long id;

    @JsonProperty("name")
    @DatabaseField(columnName = Database.ArticlesColumns.ARTICLE_TITLE)
    private String name;

    @JsonProperty("author")
    @DatabaseField(columnName = Database.ArticlesColumns.ARTICLE_PUBLISHED)
    private String author;

    @JsonProperty("imageurl")
    @DatabaseField(columnName = Database.ArticlesColumns.ARTICLE_ICON)
    private String imageUrl;

    @JsonProperty("publication_date")
    @JsonDeserialize(using = SimpleDateDeserializer.class)
    @DatabaseField(columnName = Database.ArticlesColumns.ARTICLE_PUBLISHED, dataType = DataType.DATE_LONG)
    private Date pubDate;

    @JsonProperty("avg_review")
    @DatabaseField(columnName = Database.ArticlesColumns.ARTICLE_RATE)
    private float rating;

    @JsonProperty("abstract")
    private String summary;

    @JsonProperty("content")
    private String content;

    @JsonProperty("level")
    private int level;

    @JsonProperty("gallery")
    private ArrayList<Photo> gallery;

    @JsonProperty("friendly_url")
    private String friendlyUrl;

    @Override
    public String getListingId() {
        return null;
    }

    @Override
    public Map<String, String> getLevelFieldsMap() {
        return null;
    }


    public Article() {
    }

    private Article(Parcel in) {
        id = in.readLong();
        name = in.readString();
        author = in.readString();
        imageUrl = in.readString();
        pubDate = new Date(in.readLong());
        rating = in.readFloat();
        summary = in.readString();
        content = in.readString();
        level = in.readInt();
        gallery = in.readArrayList(Photo.class.getClassLoader());
        friendlyUrl = in.readString();
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
        out.writeString(summary);
        out.writeString(content);
        out.writeInt(level);
        out.writeList(gallery);
        out.writeString(friendlyUrl);
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
