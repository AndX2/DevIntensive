package com.softdesign.devintensive.ui.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.softdesign.devintensive.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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
    public static String CALLBACK_URL = "https://github.com/AndX2/DevIntensive";

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

                if (url.contains(accessCodeFragment)) {
                    // the GET request contains an authorization code
                    String accessCode = url.substring(url.indexOf(accessCodeFragment) + accessCodeFragment.length());
                    Log.d(TAG, "accessCode = " + accessCode);

                    AsyncPost asyncPost = new AsyncPost();
                    String[] strings = {accessCode};
                    asyncPost.execute(strings);

                }
                return false;
            }

        });
        mWebView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class AsyncPost extends AsyncTask<String, Void, String>{

        private String getPostDataString(HashMap<String, String> params){
            StringBuilder result = new StringBuilder();
            boolean first = true;
            try {
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");
                    result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.d(TAG, "Error getPostDataString");
            }
            Log.d(TAG, "getPostDataString = " + result.toString());
            return result.toString();
        }

        @Override
        protected String doInBackground(String... strings) {
            String accessCode = strings[0];
            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("grant_type", "authorization_code");
            postDataParams.put("client_id", CLIENT_ID);
            postDataParams.put("client_secret", CLIENT_SECRET);
            postDataParams.put("code", accessCode);
            postDataParams.put("redirect_uri", CALLBACK_URL);
            try {
                HttpURLConnection httpURLConnection =
                        (HttpURLConnection) new URL(OAUTH_ACCESS_TOKEN_URL).openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setReadTimeout(30000);
                httpURLConnection.setConnectTimeout(30000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                outputStream.close();
                if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                            httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = bufferedReader.readLine()) != null) {
                        response.append(inputLine);
                    }
                    bufferedReader.close();

                    Log.d(TAG, "response = " + response.toString());
                    return response.toString();

                } else {
                    Log.d(TAG, "POST request not worked");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            if ((response == null) || (response.length() < 10)) return;
            String[] partsResponse = response.split("&");
            String accessToken = partsResponse[0].substring(partsResponse[0].indexOf("=") + 1);
            Log.d(TAG, "accessToken = " + accessToken);
            onBackPressed();

        }
    }

}
