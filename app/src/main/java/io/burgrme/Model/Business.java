package io.burgrme.Model;

import java.io.Serializable;

/**
 * Created by Eric on 1/15/2017.
 */

public class Business implements Serializable {
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
}
