package io.burgrme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import io.burgrme.Model.Business;

/**
 * Created by Joe on 1/20/2017.
 */

public class SharedFunctions {

    public void navigateToBusiness(Business thisBusiness, Activity activity) {
        String geoUri = "http://maps.google.com/maps?q=loc:" + thisBusiness.latitude + "," + thisBusiness.longitude + " (" + thisBusiness.name + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        activity.startActivity(intent);
    }

    public void openBusinessWebsite(Business thisBusiness, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(thisBusiness.url));
        activity.startActivity(intent);
    }

    public void shareBusiness(Business thisBusiness, Activity activity) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, thisBusiness.url);
        sendIntent.setType("text/plain");
        activity.startActivity(sendIntent);
    }

    public void callBusiness(Business thisBusiness, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + thisBusiness.phone));
        activity.startActivity(intent);
    }

    public void openBusinessMenu(Business thisBusiness, Activity activity) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.yelp.com/menu/".concat(thisBusiness.id)));
        activity.startActivity(browserIntent);
    }
}
