package io.burgrme;

import android.app.Application;
import android.preference.PreferenceManager;

import com.ftinc.scoop.Scoop;

import io.realm.Realm;

/**
 * Created by Joe on 12/21/2016.
 */

public class BaseApplication extends Application {


    @Override
    public void onCreate(){
        super.onCreate();

        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);

        Scoop.waffleCone()
                //This Default Option is placeholder does not work
                .addFlavor("Helpme", R.style.Theme_Light, true)
                .addFlavor("Light", R.style.Theme_Light)
                .addFlavor("Dark", R.style.Theme_Dark)
                .setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this))
                .initialize();
    }

}
