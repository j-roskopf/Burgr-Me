package io.burgrme.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Category;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.burgrme.Constants;
import io.burgrme.Logging.Logger;
import io.burgrme.R;

/**
 * Created by Joe on 1/2/2017.
 */

public class OverviewFragment extends Fragment {

    /**
     * UI
     */
    @BindView(R.id.businessName)
    TextView businessName;    
    
    @BindView(R.id.businessSnippet)
    TextView businessSnippet;

    @BindView(R.id.businessImage)
    ImageView businessImage;
    
    @BindView(R.id.btn_phone)
    ImageButton btn_phone;

    @BindView(R.id.btn_share)
    ImageButton btn_share;

    @BindView(R.id.btn_website)
    ImageButton btn_website;

    @BindView(R.id.btn_map)
    ImageButton btn_map;

    /**
     * 
     * NON UI
     */

    Business thisBusiness;
    Context mContext;
    Logger logger;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        initVars();


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            thisBusiness = (Business)bundle.getSerializable(Constants.BUNDLE_EXTRA_BUSINESS);
            businessName.setText(thisBusiness.name());
            businessSnippet.setText(getBusinessCategories(thisBusiness.categories()));
            btn_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBusiness(thisBusiness);
                }
            });
            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareBusiness(thisBusiness);
                }
            });
            btn_website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openBusinessWebsite(thisBusiness);
                }
            });
            btn_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateToBusiness(thisBusiness);
                }
            });
            logger.log("Business " + thisBusiness.name() + " Distance = " + thisBusiness.distance() + " mobile url = " + thisBusiness.mobileUrl() + " descript = " + thisBusiness.snippetText());

            
            //refer to these 2 SO as to why this is done
            //http://stackoverflow.com/questions/22000077/how-to-request-larger-images-from-yelp-api
            //http://stackoverflow.com/questions/17965691/yelp-api-ios-getting-a-larger-image
            String imageUrl = thisBusiness.imageUrl().replace("ms.jpg","l.jpg");


            Glide.with(this).load(imageUrl).into(businessImage);
        }else{
            businessName.setText("NULL");
        }

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Open up " + thisBusiness.name() + " in detail detail view",Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private String getBusinessCategories(ArrayList<Category> categories) {
        String toReturn = "";
        for(Category cat: categories) toReturn = toReturn.concat(cat.name() + " ");
        return toReturn;
    }

    private void navigateToBusiness(Business thisBusiness) {
        String geoUri = "http://maps.google.com/maps?q=loc:" + thisBusiness.location().coordinate().latitude() + "," + thisBusiness.location().coordinate().longitude() + " (" + thisBusiness.name() + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        getActivity().startActivity(intent);
    }

    private void openBusinessWebsite(Business thisBusiness) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(thisBusiness.mobileUrl()));
        startActivity(i);
    }

    private void shareBusiness(Business thisBusiness) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, thisBusiness.mobileUrl());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void callBusiness(Business thisBusiness) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + thisBusiness.phone()));
        getActivity().startActivity(intent);
    }

    private void initVars(){
        logger = new Logger();
    }
}
