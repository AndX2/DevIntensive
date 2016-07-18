package com.softdesign.devintensive.data.managers;

import com.softdesign.devintensive.net.RestService;
import com.softdesign.devintensive.net.ServiceGenerator;
import com.softdesign.devintensive.net.request.UserLoginRequest;
import com.softdesign.devintensive.net.response.UserListRes;
import com.softdesign.devintensive.pojo.UserProfile;
import com.softdesign.devintensive.utils.GsonHelper;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
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

    public Call<ResponseBody> uploadUserPhoto( String userId, MultipartBody.Part photo){
        return mRestService.uploadUserPhoto(userId, photo);
    }

    public Call<UserListRes> getUserList(){
        return mRestService.getUserList();
    }

}
