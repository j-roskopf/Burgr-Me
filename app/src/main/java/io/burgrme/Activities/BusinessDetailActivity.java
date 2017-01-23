package io.burgrme.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anton46.collectionitempicker.Item;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.burgrme.Constants;
import io.burgrme.Model.Business;
import io.burgrme.R;
import io.burgrme.SharedFunctions;
import io.realm.Realm;
import io.realm.RealmResults;

public class BusinessDetailActivity extends AppCompatActivity  {

    /**
     * NON UI
     */

    private Business currentBusiness;

    private SharedFunctions sharedFunctions = new SharedFunctions();

    private Context context;

    private Realm realm;

    /**
     * UI
     */

    @BindView(R.id.mainBackdrop)
    ImageView mainBackdrop;

    @BindView(R.id.baseLayout)
    CoordinatorLayout baseLayout;

    @BindView(R.id.dollarText)
    TextView dollarText;

    @BindView(R.id.distanceText)
    TextView distanceText;

    @BindView(R.id.addressText)
    TextView addressText;

    @BindView(R.id.phoneText)
    TextView phoneText;

    @BindView(R.id.ratingText)
    TextView ratingText;

    @BindView(R.id.reviewText)
    TextView reviewText;

    @BindView(R.id.directionsButton)
    ImageView directionsButton;

    @BindView(R.id.phoneButton)
    ImageView phoneButton;

    @BindView(R.id.menuButton)
    ImageView menuButton;

    @BindView(R.id.collectionPicker)
    com.anton46.collectionitempicker.CollectionPicker collectionPicker;

    @BindView(R.id.favoriteButton)
    android.support.design.widget.FloatingActionButton favoriteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail);
        ButterKnife.bind(this);

        context = this;
        realm = Realm.getDefaultInstance();

        if(getIntent().getExtras().getParcelable(Constants.BUNDLE_EXTRA_BUSINESS) != null){
            currentBusiness = (Business) getIntent().getExtras().getParcelable(Constants.BUNDLE_EXTRA_BUSINESS);

            Log.d("D","currentBusiness = " + currentBusiness);

            setBusinessInformation();
        }else{
            Snackbar.make(baseLayout,"Error", Snackbar.LENGTH_SHORT).show();
            //finish();
        }
    }

    /**
     * Displays business info
     */
    private void setBusinessInformation(){
        Glide.with(this).load(currentBusiness.image_url).into(mainBackdrop);

        //Set name
        getSupportActionBar().setTitle(currentBusiness.name);

        //Set dollar
        dollarText.setText(currentBusiness.price);

        //Set menu
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedFunctions.openBusinessMenu(currentBusiness,(Activity)context);
            }
        });

        //set Call button
        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedFunctions.callBusiness(currentBusiness,(Activity)context);
            }
        });

        //set directions button
        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedFunctions.navigateToBusiness(currentBusiness,(Activity)context);
            }
        });

        //Set Categories
        String[] categories = currentBusiness.categories.split(",");
        List<Item> items = new ArrayList<>();
        for(int i = 0; i < categories.length; i++){
            if(!categories[i].equals("")){
                items.add(new Item(categories[i],categories[i]));
            }
        }
        collectionPicker.setItems(items);

        //Set distance in meters
        Double distanceInMeters = Double.parseDouble(currentBusiness.distance);
        distanceInMeters = distanceInMeters * 0.000621371;
        distanceText.setText(String.valueOf(Math.round(distanceInMeters)).concat(" Miles away"));

        //set address
        addressText.setText(currentBusiness.display_address);

        //set phone
        phoneText.setText(currentBusiness.display_phone);

        //Set Rating
        ratingText.setText(currentBusiness.rating);

        //Set Review count
        reviewText.setText(currentBusiness.review_count);

        //setup favorite button
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBusiness(currentBusiness);
            }
        });

    }

    /**
     * Saves business to database
     * @param businessToSave
     */
    private void saveBusiness(final Business businessToSave){
        if(!inDatabaseAlready(businessToSave)){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Add a person
                    Business business = realm.createObject(Business.class);
                    business.name = businessToSave.name;
                    business.rating = businessToSave.rating;
                    business.review_count = businessToSave.review_count;
                    business.phone = businessToSave.phone;
                    business.display_phone = businessToSave.display_phone;
                    business.latitude = businessToSave.latitude;
                    business.longitude = businessToSave.longitude;
                    business.categories = businessToSave.categories;
                    business.url = businessToSave.url;
                    business.display_address = businessToSave.display_address;
                    business.price = businessToSave.price;
                    business.image_url = businessToSave.image_url;
                    business.distance = businessToSave.distance;
                    business.id = businessToSave.id;
                    Snackbar.make(baseLayout,"Successful",Snackbar.LENGTH_SHORT).show();
                }
            });
        }else{
            Snackbar.make(baseLayout,"Already Favorited!",Snackbar.LENGTH_SHORT).show();
        }

    }

    /**
     * Checks to see if business is already in the database
     * @param businessToCheck
     */
    private boolean inDatabaseAlready(Business businessToCheck){
        RealmResults<Business> results = realm.where(Business.class).equalTo("name",businessToCheck.name).findAll();
        if(results.size() > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }




}
