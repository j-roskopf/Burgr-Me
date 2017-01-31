package io.burgrme.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yelp.clientlib.entities.Category;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.burgrme.Activities.BusinessDetailActivity;
import io.burgrme.Activities.FullScreenImageViewActivity;
import io.burgrme.Constants;
import io.burgrme.Logging.Logger;
import io.burgrme.Model.Business;
import io.burgrme.R;
import io.burgrme.SharedFunctions;

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

    @BindView(R.id.businessInfoContainer)
    RelativeLayout businessInfoContainer;
    
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
    Logger logger;

    SharedFunctions sharedFunctions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, rootView);

        initVars();


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            thisBusiness = (Business) bundle.getParcelable(Constants.BUNDLE_EXTRA_BUSINESS);
            businessName.setText(thisBusiness.name);
            businessSnippet.setText(thisBusiness.categories);
            btn_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharedFunctions.callBusiness(thisBusiness, getActivity());
                }
            });
            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharedFunctions.shareBusiness(thisBusiness, getActivity());
                }
            });
            btn_website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharedFunctions.openBusinessWebsite(thisBusiness, getActivity());
                }
            });
            btn_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharedFunctions.navigateToBusiness(thisBusiness, getActivity());
                }
            });

            businessImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), FullScreenImageViewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(Constants.IMAGE_URL, thisBusiness.image_url);
                    getActivity().startActivity(intent);
                }
            });

            //refer to these 2 SO as to why this is done
            //http://stackoverflow.com/questions/22000077/how-to-request-larger-images-from-yelp-api
            //http://stackoverflow.com/questions/17965691/yelp-api-ios-getting-a-larger-image
            String imageUrl = thisBusiness.image_url;
            Glide.with(this).load(imageUrl).into(businessImage);

            businessInfoContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), BusinessDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(Constants.BUNDLE_EXTRA_BUSINESS, thisBusiness);
                    getActivity().startActivity(intent);
                }
            });
        }else{
            businessName.setText("NULL");
        }


        return rootView;
    }

    private String getBusinessCategories(ArrayList<Category> categories) {
        String toReturn = "";
        for(Category cat: categories) toReturn = toReturn.concat(cat.name() + " ");
        return toReturn;
    }

    private void initVars(){
        logger = new Logger();
        sharedFunctions = new SharedFunctions();
    }
}
