package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;

@ApiResource(Resource.ARTICLE)
public class ArticleResult extends BaseResult<Article> {


    public ArticleResult() {
    }

    private ArticleResult(Parcel in) {
        super(in);
    }

    public static final Creator<ArticleResult> CREATOR
            = new Creator<ArticleResult>() {

        @Override
        public ArticleResult createFromParcel(Parcel parcel) {
            return new ArticleResult(parcel);
        }

        @Override
        public ArticleResult[] newArray(int i) {
            return new ArticleResult[i];
        }
    };

}
