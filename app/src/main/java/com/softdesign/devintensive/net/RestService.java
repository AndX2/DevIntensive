package com.softdesign.devintensive.net;

import com.softdesign.devintensive.net.request.UserLoginRequest;
import com.softdesign.devintensive.pojo.UserProfile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by savos on 10.07.2016.
 */

public interface RestService {

    @POST("login")
    Call<UserProfile> loginUser (@Body UserLoginRequest request);


}
