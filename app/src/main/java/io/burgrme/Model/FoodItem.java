package io.burgrme.Model;

import android.os.Parcel;
import android.os.Parcelable;

import javax.inject.Inject;

/**
 * Created by Joe on 12/21/2016.
 */

public class FoodItem implements Parcelable {

    @Inject
    public FoodItem(String drawable, String name){
        this.drawable = drawable;
        this.name = name;
    }

    private String drawable;
    private String name;

    public String getDrawable() {
        return drawable;
    }

    public void setDrawable(String drawable) {
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected FoodItem(Parcel in) {
        drawable = in.readString();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(drawable);
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FoodItem> CREATOR = new Parcelable.Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };
}