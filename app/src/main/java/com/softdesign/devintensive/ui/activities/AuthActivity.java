package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.validator.TextValueValidator;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity {

    @BindView(R.id.et_email_auth) EditText mUserEmailAuth;
    @BindView(R.id.et_pass_auth) EditText mUserPassAuth;
    @BindView(R.id.activity_auth_root) LinearLayout rootView;
    @BindView(R.id.btn_auth) Button mBtnEnter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
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

    @OnClick({R.id.iv_vk_auth, R.id.iv_git_auth})
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
        }

    }


    private void showSnackbar(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }

    @OnClick({R.id.btn_auth})
    public void actionIntent(View view){
            }

}
