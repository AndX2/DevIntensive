package com.softdesign.devintensive.data.managers;

/**
 * Created by savos on 29.06.2016.
 */

public class DataManager {

    private static DataManager instance = null;
    private PreferenceManager mPreferenceManager;

    private DataManager() {
        this.mPreferenceManager = new PreferenceManager();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }

}
