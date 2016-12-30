package io.burgrme.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.burgrme.BuildConfig;
import io.burgrme.Constants;
import io.burgrme.Dagger.Components.AppComponent;
import io.burgrme.Dagger.Components.DaggerAppComponent;
import io.burgrme.Dagger.Modules.AppModule;
import io.burgrme.Model.FoodItem;
import io.burgrme.R;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.loading_spinner)
    FrameLayout loading_spinner;

    FoodItem currentFoodItem;
    YelpAPI yelpAPI;

    Context mContext;

    double mLatitude;
    double mLongitude;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 1;

    private String mLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppComponent component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        component.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        mContext = this;
        currentFoodItem = getIntent().getParcelableExtra(Constants.INTENT_EXTRA_FOOD_ITEM);

        Log.d("D","detailDebug");

        if(currentFoodItem != null){
            YelpAPIFactory apiFactory = new YelpAPIFactory(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET, BuildConfig.TOKEN, BuildConfig.TOKEN_SECRET);
            yelpAPI = apiFactory.createAPI();
            getLocationWrapper();
        }else{

        }

    }
    private void getLocationWrapper() {
        int hasWriteContactsPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
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
            if ( ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.d("D", "detailDebug geo-Location accuracy " + location.getAccuracy());
            }
            else    {
                Log.d("D", "detailDebug geo-Location Perm Not Granted ");
            }

            if (location != null) {
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(mLatitude, mLongitude, 1);
                    if (addresses.size() > 0) {
                        Log.d("D", "detailDebug geo-Location " + mLatitude + " " + mLongitude + " locality = " + addresses.get(0).getLocality());
                        searchBusinesses(addresses.get(0).getLocality());
                    }
                    else {
                        // do your staff
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else    {
            Log.d("D", "detailDebug geo-Location locationManager is null");
        }
    }

    private void showZipCodeChooser() {
    }


    public void searchBusinesses(final String location) {

        loading_spinner.setVisibility(View.VISIBLE);

        Observable.fromCallable(new Callable<Response<SearchResponse>>() {
            @Override
            public Response<SearchResponse> call() throws Exception {
                Map<String, String> params = new HashMap<>();
                params.put("term", currentFoodItem.getName());
                Call<SearchResponse> call = yelpAPI.search(location, params);
                Response<SearchResponse> response = null;
                try{
                    Log.d("D","detailDebug making call");
                    response = call.execute();
                }catch (Exception e){
                    Log.d("D","detailDebug exception with e = " + e.getMessage() + " " + e);
                }
                return response;
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<SearchResponse>>() {
            @Override
            public void onCompleted() {
                Log.d("D","detailDebug on Completed");
            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading_spinner.setVisibility(View.INVISIBLE);
                    }
                });
                Log.d("D","detailDebug on Error e = " + e);
            }

            @Override
            public void onNext(final Response<SearchResponse> response) {
                Log.d("D","detailDebug onNext with size = " + response.body().businesses().size());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        renderView(response.body().businesses().get(0));
                    }
                });

            }
        });

    }

    private void renderView(Business business) {
        Log.d("D","detailDebug Business Name = " + business.name());
        loading_spinner.setVisibility(View.INVISIBLE);
        textView.setText(business.name());
    }

}
