package io.burgrme.Retrofit;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.burgrme.BuildConfig;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by Eric on 12/27/2016.
 */

public class YelpServiceFactory {

    private static final String JSON = "application/json";

    private static YelpService mService;

    private static final String HMAC_SHA1 = "HMAC-SHA1";

    private static final String ENC = "UTF-8";

    private static Base64 base64 = new Base64();

    private static String key = BuildConfig.CONSUMER_KEY;
    private static String secret = BuildConfig.CONSUMER_SECRET;






    public static YelpService getService() {
        if (mService == null) {

            Gson gson =
                    new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            .create();

            // add the auth token to each request (if one exists)
            RequestInterceptor requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    Log.d("D","detailDebug intercepting");


                }
            };

            // if we ever get a 401, invalidate the auth token
            ErrorHandler errorHandler = new ErrorHandler() {
                @Override
                public Throwable handleError(RetrofitError cause) {
                    // only invalidate the auth token if 401 was
                    // NOT due to the user being unconfirmed

                    return cause;
                }
            };

            RestAdapter.Builder adapterBuilder =
                    new RestAdapter.Builder()
                            .setEndpoint(BuildConfig.API_SEARCH_ENDPOINT)
                            .setRequestInterceptor(requestInterceptor)
                            .setErrorHandler(errorHandler)
                            .setClient(new OkClient(new OkHttpClient()))
                            .setConverter(new GsonConverter(gson));

            mService = adapterBuilder.build().create(YelpService.class);
        }
        return mService;
    }

    public static void setService(YelpService service) {
        mService = service;
    }



    /**
     *
     * @param url
     *            the url for "request_token" URLEncoded.
     * @param params
     *            parameters string, URLEncoded.
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    private static String getSignature(String url, String params)
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {
        /**
         * base has three parts, they are connected by "&": 1) protocol 2) URL
         * (need to be URLEncoded) 3) Parameter List (need to be URLEncoded).
         */
        StringBuilder base = new StringBuilder();
        base.append("GET&");
        base.append(url);
        base.append("&");
        base.append(params);
        System.out.println("Stirng for oauth_signature generation:" + base);
        // yea, don't ask me why, it is needed to append a "&" to the end of
        // secret key.
        byte[] keyBytes = (secret + "&").getBytes(ENC);

        SecretKey key = new SecretKeySpec(keyBytes, HMAC_SHA1);

        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(key);

        // encode it, base64 it, change it to string and return.
        return new String(base64.encode(mac.doFinal(base.toString().getBytes(
                ENC))), ENC).trim();
    }

}
