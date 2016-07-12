package com.softdesign.devintensive;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by savos on 29.06.2016.
 */

public class DevIntensiveApplication extends Application {


    public static SharedPreferences sSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }


    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }
}
