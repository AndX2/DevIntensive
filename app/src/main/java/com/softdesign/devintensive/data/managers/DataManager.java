package com.softdesign.devintensive.data.managers;

import com.softdesign.devintensive.DevIntensiveApplication;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.net.RestService;
import com.softdesign.devintensive.net.ServiceGenerator;

/**
 * Created by savos on 29.06.2016.
 */

public class DataManager {

    private static DataManager instance = null;
    private PreferenceManager mPreferenceManager;
    private NetworkManager mNetworkManager;
    private DaoSession mDaoSession;



    private DataManager() {
        this.mPreferenceManager = new PreferenceManager();
        this.mDaoSession = DevIntensiveApplication.getDaoSession();
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

    public NetworkManager getNetworkManager(){
        if (mNetworkManager == null) mNetworkManager = NetworkManager.getInstance();
        return mNetworkManager;
    }


}
