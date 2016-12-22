package io.burgrme.Logging;

import android.util.Log;

import javax.inject.Inject;

import io.burgrme.BuildConfig;

/**
 * Created by Joe on 12/21/2016.
 */

public class Logger {

    private String TAG = "BurgrMe";

    @Inject
    public Logger(){

    }

    public void log(String message){
        if(BuildConfig.DEBUG){
            Log.d(TAG,message);
        }
    }
}
