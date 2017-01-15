package io.burgrme.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;

import io.burgrme.Constants;
import io.burgrme.Fragments.OverviewFragment;

/**
 * Created by Joe on 1/2/2017.
 */

public class OverviewPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Business> businesses;

    public OverviewPagerAdapter(FragmentManager fm, ArrayList<Business> businesses) {
        super(fm);
        this.businesses = businesses;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        Fragment detailFragment = new OverviewFragment();

        bundle.putSerializable(Constants.BUNDLE_EXTRA_BUSINESS,businesses.get(position));
        detailFragment.setArguments(bundle);

        return detailFragment;
    }

    @Override
    public int getCount() {
        return businesses.size();
    }
}