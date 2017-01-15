package io.burgrme.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yelp.clientlib.entities.Business;

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

    @BindView(R.id.businessImage)
    ImageView businessImage;

    /**
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

    private void initVars(){
        logger = new Logger();
    }
}
