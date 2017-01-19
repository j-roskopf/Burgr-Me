package io.burgrme.Activities;

import android.content.Intent;
import android.net.Uri;
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

public class BusinessDetailActivity extends AppCompatActivity {

    /**
     * NON UI
     */

    Business currentBusiness;

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

    @BindView(R.id.menuButton)
    ImageView menuButton;

    @BindView(R.id.collectionPicker)
    com.anton46.collectionitempicker.CollectionPicker collectionPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail);
        ButterKnife.bind(this);

        if(getIntent().getExtras().getSerializable(Constants.BUNDLE_EXTRA_BUSINESS) != null){
            currentBusiness = (Business) getIntent().getExtras().getSerializable(Constants.BUNDLE_EXTRA_BUSINESS);

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
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.yelp.com/menu/".concat(currentBusiness.id)));
                startActivity(browserIntent);
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

    }
}
