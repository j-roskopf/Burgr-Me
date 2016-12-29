package io.burgrme.Model.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.burgrme.Model.YelpBusiness;

/**
 * Created by Joe on 12/27/2016.
 */

public class YelpResponse {
    @SerializedName("businesses")
    ArrayList<YelpBusiness> mBusinesses;
}
