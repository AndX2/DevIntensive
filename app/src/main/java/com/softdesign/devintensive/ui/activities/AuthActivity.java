package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.pojo.UserInfo;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.SecurityHelper;
import com.softdesign.devintensive.utils.validator.TextValueValidator;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

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
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity {

    public static final String TAG = "AuthActivityTag";

    private String jsonUserInfo;

    @BindView(R.id.et_email_auth) EditText mUserEmailAuth;
    @BindView(R.id.et_pass_auth) EditText mUserPassAuth;
    @BindView(R.id.activity_auth_root) LinearLayout rootView;
    @BindView(R.id.btn_auth) Button mBtnEnter;
    @BindView(R.id.card_auth)
    CardView mCardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        if (!DataManager.getInstance().getUserInfoManager().isEmpty())
            mUserEmailAuth.setText(DataManager.getInstance().getUserInfoManager().geteMail());

        mUserEmailAuth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                boolean isValid = TextValueValidator.validate(TextValueValidator.Type.Email, mUserEmailAuth.getText().toString());
                if (!b) {
                    if (!isValid) {
                        mUserEmailAuth.setTextColor(Color.RED);
                        showSnackbar(getString(R.string.error_email_value_message));
                    }
                }else {
                    mUserEmailAuth.setTextColor(Color.BLACK);
                    if(!isValid) mUserEmailAuth.setText("");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (DataManager.getInstance().getPreferenceManager().loadVKAuth() != null){
//            startActivity(new Intent(this, MainActivity.class));
//        }

    }

    @OnClick({R.id.iv_vk_auth, R.id.iv_git_auth, R.id.btn_auth})
    public void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.iv_vk_auth:
                intent = new Intent(this, VKAuthActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_git_auth:
                intent = new Intent(this, GitAuthActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_auth:
                String[] authInfo = {mUserEmailAuth.getText().toString(),
                        mUserPassAuth.getText().toString()};
                AsyncPostAuth asyncPostAuth = new AsyncPostAuth();
                asyncPostAuth.execute(authInfo);
                break;
        }

    }


    private void showSnackbar(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }

    private void flashError(){
        mCardView.setBackgroundColor(Color.RED);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCardView.setBackgroundColor(Color.WHITE);;
            }
        }, ConstantManager.AUTH_ERROR_FLASH_DELAY);
    }


    private class AsyncPostAuth extends AsyncTask<String, Void, String> {

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
            String eMail = strings[0];
            String pass = strings[1];
            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("email", eMail);
            postDataParams.put("password", pass);
            try {
                HttpURLConnection httpURLConnection =
                        (HttpURLConnection) new URL(ConstantManager.API_LOGIN_URL).openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                outputStream.close();
                Log.d(TAG, "responseCode = " + httpURLConnection.getResponseCode());
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

            if ((response == null) || (response.length() < 10)){
                if (DataManager.getInstance().getPreferenceManager()
                        .checkPassFingerPrint(SecurityHelper.getMd5(mUserPassAuth.getText().toString()))){
                    startActivity(new Intent(AuthActivity.this, MainActivity.class));
                    return;
                }
                flashError();
                return;
            }
            Log.d(TAG, "onPostExecute!");
            DataManager.getInstance().getPreferenceManager()
                    .savePassFingerPrint(SecurityHelper.getMd5(mUserPassAuth.getText().toString()));
            DataManager.getInstance().getUserInfoManager().setJsonUserInfo(response);
            startActivity(new Intent(AuthActivity.this, MainActivity.class));
            //startMainActivity();

        }
    }

//    private void startMainActivity(){
//        startActivity(new Intent(this, MainActivity.class));
//
//    }

}
