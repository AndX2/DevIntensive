package com.softdesign.devintensive.net;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.softdesign.devintensive.DevIntensiveApplication;
import com.squareup.picasso.Picasso;

/**
 * Created by savos on 17.07.2016.
 */

public class PicassoCache {

    private Context mContext;
    private static Picasso mPicassoInstance;

    private PicassoCache() {
        mContext = DevIntensiveApplication.getAppContext();
        OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(mContext, Integer.MAX_VALUE);
        Picasso.Builder builder = new Picasso.Builder(mContext);
        builder.downloader(okHttp3Downloader);

        mPicassoInstance = builder.build();
        Picasso.setSingletonInstance(mPicassoInstance);
    }

    public static synchronized Picasso getPicassoInstance(){
        if (mPicassoInstance == null) {
            new PicassoCache();

        }
        return mPicassoInstance;

    }
}
