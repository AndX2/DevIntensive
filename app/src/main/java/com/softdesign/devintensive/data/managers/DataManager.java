package com.softdesign.devintensive.data.managers;

import com.softdesign.devintensive.net.RestService;
import com.softdesign.devintensive.net.ServiceGenerator;

/**
 * Created by savos on 29.06.2016.
 */

public class DataManager {

    private static DataManager instance = null;
    private PreferenceManager mPreferenceManager;
    private UserInfoManager mUserInfoManager;
    private NetworkManager mNetworkManager;


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

    public UserInfoManager getUserInfoManager(){
        if (mUserInfoManager == null) mUserInfoManager = UserInfoManager.getInstance();
        return mUserInfoManager;
    }

    public NetworkManager getNetworkManager(){
        if (mNetworkManager == null) mNetworkManager = NetworkManager.getInstance();
        return mNetworkManager;
    }


}
