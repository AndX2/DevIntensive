package com.softdesign.devintensive.ui.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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
