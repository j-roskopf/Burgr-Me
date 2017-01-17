package io.burgrme.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.burgrme.Adapters.OverviewPagerAdapter;
import io.burgrme.BuildConfig;
import io.burgrme.Constants;
import io.burgrme.Dagger.Components.AppComponent;
import io.burgrme.Dagger.Components.DaggerAppComponent;
import io.burgrme.Dagger.Modules.AppModule;
import io.burgrme.Logging.Logger;
import io.burgrme.Model.FoodItem;
import io.burgrme.Model.responses.YelpBusinessResponse;
import io.burgrme.Model.responses.YelpResponse;
import io.burgrme.R;
import io.burgrme.Retrofit.YelpServiceFactory;
import retrofit.Callback;
import retrofit.RetrofitError;

import static io.burgrme.Constants.REQUEST_CODE_ASK_PERMISSIONS;

public class OverviewActivity extends AppCompatActivity implements LocationListener {

    /**
     * UI
     */

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.loading_spinner)
    FrameLayout loading_spinner;

    /**
     * NON UI
     */

    FoodItem currentFoodItem;
    YelpAPI yelpAPI;

    Context mContext;
    SharedPreferences mPrefs;

    double mLatitude;
    double mLongitude;

    /**
     * DAGGER INJECTED
     */

    @Inject
    Logger logger;



    private String mLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppComponent component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        component.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        mContext = this;
        mPrefs = getSharedPreferences("Main",MODE_PRIVATE);
        currentFoodItem = getIntent().getParcelableExtra(Constants.INTENT_EXTRA_FOOD_ITEM);

        logger.log("detailDebug 1");

        if(currentFoodItem != null){
            YelpAPIFactory apiFactory = new YelpAPIFactory(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET, BuildConfig.TOKEN, BuildConfig.TOKEN_SECRET);
            yelpAPI = apiFactory.createAPI();
            getLocationWrapper();
        }else{
            logger.log("detailDebug 2");
        }

    }
    private void getLocationWrapper() {
        int hasWriteContactsPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);

            logger.log("detailDebug 3");

            return;
        }
        getLocation();
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{mLocationPermission},
                REQUEST_CODE_ASK_PERMISSIONS);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == REQUEST_CODE_ASK_PERMISSIONS){
            boolean permissionStatus= grantResults.length > 0;
            if(permissionStatus){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //user accepted , get location and fetch home cards
                    getLocation();
                } else if(grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    boolean shouldShowRequestPermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, mLocationPermission);
                    if(shouldShowRequestPermissionRationale){
                        //user denied without Never ask again, just show rationale explanation
                        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
                        builder.setTitle("Permission Required");
                        builder.setMessage("Burgr Me works when we know what tasty dishes are nearest you, allow us to access your location?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getLocation();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                requestPermission();
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();

                    }else{
                        //todo show zip code chooser
                        showZipCodeChooser();
                    }
                }
            }
        }
    }

    public void getLocation()   {
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {

            Location location = null;
            boolean isGPSEnabled = false;
            boolean isNetworkEnabled = false;

            if ( ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {


                // getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                logger.log("locationDebug isNetworkEnabled  + isGPSEnabled = " + isNetworkEnabled + " " + isGPSEnabled);

                if (isGPSEnabled || isNetworkEnabled) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) == null ? locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            : locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


                    if(location != null) {
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();
                        getAddressFromLatitudeAndLongitude(mLatitude, mLongitude);
                    }

                    /**
                     * request location updates, especially for emulators, as there may not be a last known location saved to the device.
                     */
                    if(isNetworkEnabled){
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                Constants.MIN_TIME_BETWEEN_UPDATES,
                                Constants.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    }else if(isGPSEnabled){
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                Constants.MIN_TIME_BETWEEN_UPDATES,
                                Constants.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    }

                }
            }
            else    {
                logger.log("detailDebug 8 geo-Location Perm Not Granted ");
            }

        }
        else    {
            logger.log("detailDebug geo-Location locationManager is null");
        }
    }

    /**
     * Given the provided lat/lon, get an address to search for on Yelp
     *
     * @param mLatitude - latitude to search
     * @param mLongitude - longitude to search
     */
    private void getAddressFromLatitudeAndLongitude(double mLatitude, double mLongitude) {
        logger.log("locationDebug lat + lon from last known location = " + mLatitude + " " + mLongitude);

        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(mLatitude, mLongitude, 1);
            if (addresses.size() > 0) {
                logger.log("detailDebug geo-Location " + mLatitude + " " + mLongitude + " locality = " + addresses.get(0).getLocality() + " currentFoodItem.getName() = " + currentFoodItem.getName());
                //searchBusinesses(addresses.get(0).getLocality());

                if(mPrefs.contains("access_token")) {
                    YelpServiceFactory.getService(mPrefs.getString("access_token","")).getBusiness(currentFoodItem.getName(), String.valueOf(mLatitude), String.valueOf(mLongitude), new Callback<YelpResponse>() {
                        @Override
                        public void success(YelpResponse yelpResponse, retrofit.client.Response response) {
                            Log.d("D", "Yelp Response size = " + yelpResponse.businesses.size());
                            ArrayList<YelpBusinessResponse> businessResponses = yelpResponse.businesses;
                            ArrayList<io.burgrme.Model.Business> business = new ArrayList<io.burgrme.Model.Business>();
                            for(YelpBusinessResponse businessResponse : businessResponses)  {
                                business.add(businessResponse.toBusiness());
                            }
                            Log.d("D", "Yelp buisness size to build adapter = " + business.size());
                            setupViewPager(business);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d("D", "Yelp Response fail");
                        }
                    });
                }
                else Log.d("D", "Yelp no access token");
            }
            else {
                // do your staff
                logger.log("detailDebug 6");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showZipCodeChooser() {
    }


    //old method
    /*public void searchBusinesses(final String location) {

        loading_spinner.setVisibility(View.VISIBLE);

        Observable.fromCallable(new Callable<Response<SearchResponse>>() {
            @Override
            public Response<SearchResponse> call() throws Exception {
                Map<String, String> params = new HashMap<>();
                params.put("term", currentFoodItem.getName());
                Call<SearchResponse> call = yelpAPI.search(location, params);
                Response<SearchResponse> response = null;
                try{
                    logger.log("detailDebug making call");
                    response = call.execute();
                }catch (Exception e){
                    logger.log("detailDebug exception with e = " + e.getMessage() + " " + e);
                }
                return response;
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<SearchResponse>>() {
            @Override
            public void onCompleted() {
                logger.log("detailDebug on Completed");
            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading_spinner.setVisibility(View.INVISIBLE);
                    }
                });
                logger.log("detailDebug on Error e = " + e);
            }

            @Override
            public void onNext(final Response<SearchResponse> response) {
                logger.log("detailDebug onNext with size = " + response.body().businesses().size());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //We only want to display a certain amount of businesses by default, so loop through all of the responses
                        //and get the first X amount where X = DEFAULT_AMOUNT_OF_BUSINESSES_TO_DISPLAY

                        ArrayList<Business> toDisplay = new ArrayList<Business>();
                        for(int i = 0; i < Constants.DEFAULT_AMOUNT_OF_BUSINESSES_TO_DISPLAY; i++){
                            if(i < response.body().businesses().size()){
                                toDisplay.add(response.body().businesses().get(i));
                            }else{
                                //Less results than our default
                                break;
                            }

                        }
                        setupViewPager(toDisplay);
                        //renderView(response.body().businesses().get(0));
                    }
                });

            }
        });
    }*/

    /**
     * Displays the array of business in a view pager
     * @param toDisplay
     */
    private void setupViewPager(ArrayList<io.burgrme.Model.Business> toDisplay) {
        //Done loading, hide the spinner
        loading_spinner.setVisibility(View.INVISIBLE);

        /**
         * If feeling lucky was clicked, just navigate the user to that location, else setup the view pager
         */
        if(getIntent().getExtras().getBoolean(Constants.FEELING_LUCKY)){
            String geoUri = "http://maps.google.com/maps?q=loc:" + toDisplay.get(0).latitude + "," + toDisplay.get(0).longitude + " (" + toDisplay.get(0).name + ")";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
            startActivity(intent);
            finish();
        }else{
            OverviewPagerAdapter detailViewPagerAdapter = new OverviewPagerAdapter(getSupportFragmentManager(),toDisplay);
            viewPager.setClipToPadding(false);
            viewPager.setPageMargin(50);
            viewPager.setPadding(150,150,150,150);
            viewPager.setAdapter(detailViewPagerAdapter);
        }


    }

    private void renderView(Business business) {
        logger.log("detailDebug Business Name = " + business.name());
        loading_spinner.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
        } catch (NullPointerException npe) {}
    }

    @Override
    public void onLocationChanged(Location location) {
        logger.log("locationDebug onLocationChanged " + location.getLatitude() + " " + location.getLongitude());

        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();

        getAddressFromLatitudeAndLongitude(mLatitude, mLongitude);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }


}
