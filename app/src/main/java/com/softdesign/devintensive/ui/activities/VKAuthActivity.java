package com.softdesign.devintensive.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.pojo.VKAuth;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by savos on 07.07.2016.
 */

public class VKAuthActivity extends BaseActivity {

    public static final String TAG = "VKAuthActivity: ";

    @BindView(R.id.auth_web_view) WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_auth_activity);
        ButterKnife.bind(this);

        String requestUrl = "https://oauth.vk.com/authorize?" +
                "client_id=" + ConstantManager.VK_APP_ID +
                "&display=mobile&redirect_uri=https://oauth.vk.com/blank.html" +
                "&scope=offline&response_type=token&v=5.52&state=123456";
        mWebView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading (WebView view, String urlString){
                Log.d(TAG, urlString);
                String response = "";
                try {
                    URL url = new URL(urlString);
                    response = url.getRef();
                    Log.d(TAG, "getRef" + response);
                    parseResponse(response);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if (response == null){
                    return false;
                }
                return true;
            }

        });
        mWebView.loadUrl(requestUrl);
    }

    private void parseResponse(String response){
        if ((response == null) || (response.length() < 10)) return;
        String[] partsResponse = response.split("&");
        String accessToken = partsResponse[0].substring(partsResponse[0].indexOf("=") + 1);
        String idUserVK = partsResponse[2].substring(partsResponse[2].indexOf("=") + 1);
        Log.d(TAG, "id = " + idUserVK + "; token = " + accessToken);
        DataManager.getInstance().getPreferenceManager().saveVKAuth(new VKAuth(idUserVK, accessToken));
        super.onBackPressed();

    }

}
