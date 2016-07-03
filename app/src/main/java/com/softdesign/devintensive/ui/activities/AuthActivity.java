package com.softdesign.devintensive.ui.activities;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.validator.TextValueValidator;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private void showSnackbar(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }
}
