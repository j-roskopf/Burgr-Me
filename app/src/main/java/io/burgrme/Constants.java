package io.burgrme;

/**
 * Created by Joe on 12/20/2016.
 */
public class Constants {
    /**
     * KEYS
     */
    public static final String INTENT_EXTRA_FOOD_ITEM = "food_item";

    public static final String BUNDLE_EXTRA_BUSINESS = "business";

    public static final String FEELING_LUCKY = "feeling_lucky";

    public static final String IMAGE_URL = "url";

    public static final int SPEECH_KEY = 64;
    /**
     * END KEYS
     */

    // The minimum distance to change Updates in meters
    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    public static final long MIN_TIME_BETWEEN_UPDATES = 1000 * 60 * 1; // 1 minute

    public static final int REQUEST_CODE_ASK_PERMISSIONS = 1;

    public static final int DEFAULT_AMOUNT_OF_BUSINESSES_TO_DISPLAY = 5;

}

