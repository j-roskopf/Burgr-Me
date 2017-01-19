package io.burgrme.Model.responses;

import java.util.ArrayList;

import io.burgrme.Model.Business;
import io.burgrme.Model.YelpObjects.YelpCategory;
import io.burgrme.Model.YelpObjects.YelpCoordinates;
import io.burgrme.Model.YelpObjects.YelpLocation;

/**
 * Created by Eric on 1/15/2017.
 */

public class YelpBusinessResponse {
    public String name;
    public double rating;
    public int review_count;
    public String phone;
    public String display_phone;
    public YelpCoordinates coordinates;
    public ArrayList<YelpCategory> categories;
    public String url;
    public YelpLocation location;
    public String id;
    public String price;
    public String image_url;
    public double distance;

    public Business toBusiness()    {
        Business toReturn = new Business();
        toReturn.name = name;
        toReturn.id = id;
        toReturn.rating = String.valueOf(rating);
        toReturn.review_count = String.valueOf(review_count);
        toReturn.phone = phone;
        toReturn.display_phone = display_phone;
        toReturn.latitude = String.valueOf(coordinates.latitude);
        toReturn.longitude = String.valueOf(coordinates.longitude);
        String businessCategories = "";
        for(YelpCategory cat : categories)  {
            businessCategories = businessCategories.concat(cat.title + ",");
        }
        toReturn.categories = businessCategories;
        toReturn.url = url;
        if(location.display_address.length > 0) toReturn.display_address = location.display_address[0];
        toReturn.price = price;
        toReturn.image_url = image_url;
        toReturn.distance = String.valueOf(distance);

        return toReturn;
    }
}

