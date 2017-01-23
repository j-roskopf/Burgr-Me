package io.burgrme.Model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by Eric on 1/15/2017.
 */

public class Business extends RealmObject implements Parcelable {
    public String name;
    public String rating;
    public String review_count;
    public String phone;
    public String display_phone;
    public String latitude;
    public String longitude;
    public String categories;
    public String url;
    public String display_address;
    public String price;
    public String image_url;
    public String distance;
    public String id;

    public Business(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview_count() {
        return review_count;
    }

    public void setReview_count(String review_count) {
        this.review_count = review_count;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDisplay_phone() {
        return display_phone;
    }

    public void setDisplay_phone(String display_phone) {
        this.display_phone = display_phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplay_address() {
        return display_address;
    }

    public void setDisplay_address(String display_address) {
        this.display_address = display_address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Business{" +
                "name='" + name + '\'' +
                ", rating='" + rating + '\'' +
                ", review_count='" + review_count + '\'' +
                ", phone='" + phone + '\'' +
                ", display_phone='" + display_phone + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", categories='" + categories + '\'' +
                ", url='" + url + '\'' +
                ", display_address='" + display_address + '\'' +
                ", price='" + price + '\'' +
                ", image_url='" + image_url + '\'' +
                ", id='" + id + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }

    protected Business(Parcel in) {
        name = in.readString();
        rating = in.readString();
        review_count = in.readString();
        phone = in.readString();
        display_phone = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        categories = in.readString();
        url = in.readString();
        display_address = in.readString();
        price = in.readString();
        image_url = in.readString();
        distance = in.readString();
        id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(rating);
        dest.writeString(review_count);
        dest.writeString(phone);
        dest.writeString(display_phone);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(categories);
        dest.writeString(url);
        dest.writeString(display_address);
        dest.writeString(price);
        dest.writeString(image_url);
        dest.writeString(distance);
        dest.writeString(id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Business> CREATOR = new Parcelable.Creator<Business>() {
        @Override
        public Business createFromParcel(Parcel in) {
            return new Business(in);
        }

        @Override
        public Business[] newArray(int size) {
            return new Business[size];
        }
    };
}