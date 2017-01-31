package io.burgrme.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import io.burgrme.R;

/**
 * Created by Joe on 1/18/2017.
 */

public class ImageViewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, rootView);


        return rootView;
    }
}
