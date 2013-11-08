package com.arcasolutions.api.model;

import android.os.Parcel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper = false)
public class ArticleCategory extends BaseCategory {

    @JsonProperty("active_articles")
    private int activeItems;

    public ArticleCategory() {
    }

    private ArticleCategory(Parcel in) {
        super(in);
        activeItems = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        super.writeToParcel(out, flag);
        out.writeInt(activeItems);
    }

    public static final Creator<ArticleCategory> CREATOR
            = new Creator<ArticleCategory>() {

        @Override
        public ArticleCategory createFromParcel(Parcel parcel) {
            return new ArticleCategory(parcel);
        }

        @Override
        public ArticleCategory[] newArray(int i) {
            return new ArticleCategory[i];
        }
    };

}
