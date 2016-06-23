package com.softdesign.devintensive.ui.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;

public class MainActivity extends AppCompatActivity {

    Button mRedButton, mGreenButton;
    EditText mEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(ConstantManager.TAG_PREFIX, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(ConstantManager.TAG_PREFIX, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(ConstantManager.TAG_PREFIX, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(ConstantManager.TAG_PREFIX, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(ConstantManager.TAG_PREFIX, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(ConstantManager.TAG_PREFIX, "onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(ConstantManager.TAG_PREFIX, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(ConstantManager.TAG_PREFIX, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(ConstantManager.TAG_PREFIX, "onRestoreInstanceState");
    }

}
