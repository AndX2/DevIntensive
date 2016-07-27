package com.softdesign.devintensive.net.interceptors;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.pojo.UserProfile;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by savos on 13.07.2016.
 */

public class HeaderInterceptors implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        UserProfile userProfile = DataManager.getInstance().getPreferenceManager().loadUserProfile();
        if (userProfile == null) return chain.proceed(chain.request());
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("X-Access-Token", userProfile.getData().getToken())
                .header("Request-User-Id", userProfile.getData().getUser().getId())
                .header("User-Agent", "DevIntensive")
                .header("Cache-control", "max-age=" + (60+60+24));

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
