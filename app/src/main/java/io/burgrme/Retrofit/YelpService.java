package io.burgrme.Retrofit;

import java.util.Map;

import io.burgrme.Model.responses.YelpResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.QueryMap;


/**
 * Created by Joe on 12/27/2016.
 */

public interface YelpService {

        @GET("/v2/search")
        void getBusinesses(
                Callback<YelpResponse> callback);

        @GET("/v2/search")
        void search(
                @QueryMap Map<String, String> params,
                Callback<YelpResponse> callback);
}
