package com.arcasolutions.ui.fragment.map;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcasolutions.api.model.BaseResult;
import com.arcasolutions.api.model.ClassifiedResult;
import com.arcasolutions.api.model.DealResult;
import com.arcasolutions.api.model.EventResult;
import com.arcasolutions.api.model.ListingResult;

import java.util.List;

import lombok.Data;

@Data
public class Filter implements Parcelable {

    public interface ModuleClass {
        Class<? extends BaseResult> LISTING = ListingResult.class;
        Class<? extends BaseResult> DEAL = DealResult.class;
        Class<? extends BaseResult> CLASSIFIED = ClassifiedResult.class;
        Class<? extends BaseResult> EVENT = EventResult.class;
    }

    public interface ModuleIndex {
        int LISTING = 0;
        int DEAL = 1;
        int CLASSIFIED = 2;
        int EVENT = 3;
    }

    private String keyword;

    private String location;

    private int moduleIndex;

    private List<Integer> ratings;

    private String category;

    private int categoryIndex = 0;


    @Override
    public int describeContents() {
        return 0;
    }

    public Filter() {}

    private Filter(Parcel in) {
        keyword = in.readString();
        location = in.readString();
        moduleIndex = in.readInt();
        in.readList(ratings, Integer.class.getClassLoader());
        category = in.readString();
        categoryIndex = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(keyword);
        out.writeString(location);
        out.writeInt(moduleIndex);
        out.writeList(ratings);
        out.writeString(category);
        out.writeInt(categoryIndex);
    }

    public static Class<? extends BaseResult> getModuleClass(int moduleIndex) {
        switch (moduleIndex) {

            case Filter.ModuleIndex.DEAL:
                return Filter.ModuleClass.DEAL;

            case Filter.ModuleIndex.CLASSIFIED:
                return Filter.ModuleClass.CLASSIFIED;

            case Filter.ModuleIndex.EVENT:
                return Filter.ModuleClass.EVENT;

            case Filter.ModuleIndex.LISTING:
            default:
                return Filter.ModuleClass.LISTING;
        }
    }

    public static Parcelable.Creator<Filter> CREATOR
            = new Parcelable.Creator<Filter>() {

        @Override
        public Filter createFromParcel(Parcel parcel) {
            return new Filter(parcel);
        }

        @Override
        public Filter[] newArray(int i) {
            return new Filter[i];
        }
    };

}
