package io.burgrme.Retrofit;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * Created by Joe on 12/27/2016.
 */

/**
 * Service provider for "2-legged" OAuth10a for Yelp API (version 2).
 */
public class YelpAPI extends DefaultApi10a {

    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    @Override
    public String getAuthorizationUrl(Token arg0) {
        return null;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }

}