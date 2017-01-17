package io.burgrme.Model;

import android.os.Parcel;
import android.os.Parcelable;

import javax.inject.Inject;

/**
 * Created by Joe on 12/21/2016.
 */

public class FoodItem implements Parcelable {

    @Inject
    public FoodItem(String drawable, String name, String foodType){
        this.drawable = drawable;
        this.name = name;
        this.foodType = foodType;
    }

    private String drawable;
    private String name;
    private String foodType;

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

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    protected FoodItem(Parcel in) {
        drawable = in.readString();
        name = in.readString();
        foodType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(drawable);
        dest.writeString(name);
        dest.writeString(foodType);
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