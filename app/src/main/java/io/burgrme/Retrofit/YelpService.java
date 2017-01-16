package io.burgrme.Retrofit;

import java.util.Map;

import io.burgrme.Model.responses.YelpResponse;
import io.burgrme.Model.responses.YelpTokenResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.POST;


/**
 * Created by Joe on 12/27/2016.
 */

public interface YelpService {

        @POST("/oauth2/token")
        void getToken(
                @Query("client_id") String query,
                @Query("client_secret") String latitude,
                Callback<YelpTokenResponse> callback);

        @GET("/v3/businesses/search")
        void getBusiness(
                @Query("term") String query,
                @Query("latitude") String latitude,
                @Query("longitude") String longitude,
                Callback<YelpResponse> callback);

}
