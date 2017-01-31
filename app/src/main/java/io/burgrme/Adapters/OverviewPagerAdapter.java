package io.burgrme.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import io.burgrme.Constants;
import io.burgrme.Fragments.OverviewFragment;
import io.burgrme.Model.Business;

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
        Fragment overviewFragment = new OverviewFragment();
                bundle.putParcelable(Constants.BUNDLE_EXTRA_BUSINESS,businesses.get(position));
        overviewFragment.setArguments(bundle);

        return overviewFragment;
    }

    @Override
    public int getCount() {
        return businesses.size();
    }
}
