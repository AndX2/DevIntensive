package com.softdesign.devintensive.data.managers;

import com.softdesign.devintensive.net.RestService;
import com.softdesign.devintensive.net.ServiceGenerator;
import com.softdesign.devintensive.net.request.UserLoginRequest;
import com.softdesign.devintensive.pojo.UserProfile;

import retrofit2.Call;

/**
 * Created by savos on 10.07.2016.
 */

public class NetworkManager {

    private static NetworkManager instance;

    private static RestService mRestService;

    private NetworkManager(){
        mRestService = ServiceGenerator.createService(RestService.class);
    }

    synchronized public static NetworkManager getInstance(){
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    public Call<UserProfile> loginUser(UserLoginRequest userLoginRequest){
        return mRestService.loginUser(userLoginRequest);
    }

}
