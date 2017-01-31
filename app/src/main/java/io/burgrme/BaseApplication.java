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
                .addFlavor("Default", R.style.Theme_Scoop_Default, true)
                .addFlavor("Light", R.style.Theme_Scoop_Dark)
                .setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this))
                .initialize();
    }

}
