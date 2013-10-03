package com.arcasolutions.api.model;

import android.os.Parcel;

import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;

@ApiResource(Resource.ARTICLE_CATEGORY)
public class ArticleCategoryResult extends BaseCategoryResult<ArticleCategory> {

    public ArticleCategoryResult() {}

    private ArticleCategoryResult(Parcel in) {
        super(in);
    }

    public static final Creator<ArticleCategoryResult> CREATOR
            = new Creator<ArticleCategoryResult>() {

        @Override
        public ArticleCategoryResult createFromParcel(Parcel parcel) {
            return new ArticleCategoryResult(parcel);
        }

        @Override
        public ArticleCategoryResult[] newArray(int i) {
            return new ArticleCategoryResult[i];
        }
    };

}
