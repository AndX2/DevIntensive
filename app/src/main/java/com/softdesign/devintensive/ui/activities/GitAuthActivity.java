package com.softdesign.devintensive.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.softdesign.devintensive.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by savos on 07.07.2016.
 */

public class GitAuthActivity extends BaseActivity {

    public static String OAUTH_URL = "https://github.com/login/oauth/authorize";
    public static String OAUTH_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";

    public static String CLIENT_ID = "47ea27470ddeb8dd366f";
    public static String CLIENT_SECRET = "07d25fa9678c0b7cbc9c50bbf4d0b7bb05d127db";
    public static String CALLBACK_URL = "http://localhost";

    public static final String TAG = "GitAuthActivity: ";

    @BindView(R.id.auth_web_view) WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_auth_activity);
        ButterKnife.bind(this);

        String url = OAUTH_URL + "?client_id=" + CLIENT_ID;

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String accessTokenFragment = "access_token=";
                String accessCodeFragment = "code=";

                // We hijack the GET request to extract the OAuth parameters
                Log.d(TAG, "url = " + url);

                if (url.contains(accessTokenFragment)) {
                    // the GET request contains directly the token
                    String accessToken = url.substring(url.indexOf(accessTokenFragment));
                    Log.d(TAG, " accessToken = " + accessToken);


                } else if (url.contains(accessCodeFragment)) {
                    // the GET request contains an authorization code
                    String accessCode = url.substring(url.indexOf(accessCodeFragment));
                    Log.d(TAG, "accessCode = " + accessCode);


                    String query = "client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&code=" + accessCode;
                    view.postUrl(OAUTH_ACCESS_TOKEN_URL, query.getBytes());
                }
                return false;

            }


        });
        mWebView.loadUrl(url);
    }

}
