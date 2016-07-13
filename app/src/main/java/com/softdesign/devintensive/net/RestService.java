package com.softdesign.devintensive.net;

import com.softdesign.devintensive.net.request.UserLoginRequest;
import com.softdesign.devintensive.pojo.UserProfile;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by savos on 10.07.2016.
 */

public interface RestService {

    @POST("login")
    Call<UserProfile> loginUser (@Body UserLoginRequest request);

    @Multipart
    @POST("user/{userId}/publicValues/profilePhoto")
    Call<ResponseBody> uploadUserPhoto(@Path("userId") String userId, @Part MultipartBody.Part photo);


}
