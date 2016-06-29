package com.softdesign.devintensive.ui.activities;

import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.ui.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.NavigationView.*;


public class MainActivity extends BaseActivity implements OnClickListener {

    private DataManager mDataManager;
    private int mCurrentEditMode = 0;

    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;
    private EditText mUserPhone, mUserMail, mUserVK, mUserGit, mUserMySelf;

    private List<EditText> mUserInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(ConstantManager.TAG_PREFIX, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_main);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar();
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupDrawer();

        mDataManager = DataManager.getInstance();

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);
        mUserPhone = (EditText) findViewById(R.id.et_phone);
        mUserMail = (EditText) findViewById(R.id.et_email);
        mUserVK = (EditText) findViewById(R.id.et_vk);
        mUserGit = (EditText) findViewById(R.id.et_git1);
        mUserMySelf = (EditText) findViewById(R.id.et_myself);

        mUserInfo = new ArrayList<>();
        mUserInfo.add(mUserPhone);
        mUserInfo.add(mUserMail);
        mUserInfo.add(mUserVK);
        mUserInfo.add(mUserGit);
        mUserInfo.add(mUserMySelf);

        if (savedInstanceState == null) {
            showSnackbar("Activity start is first");
        } else {
            showSnackbar("Activity is restart");
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }

        loadUserInfoValues();


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
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(ConstantManager.TAG_PREFIX, "onRestoreInstanceState");
    }

    //TODO: replace this to Log.d
    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mNavigationDrawer.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                showSnackbar("FAB onClick");
                if (mCurrentEditMode == 0) {
                    mCurrentEditMode = 1;
                    changeEditMode(1);
                } else {
                    mCurrentEditMode = 0;
                    changeEditMode(0);
                }
                break;
        }
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar("Drawer item selected " + item.getTitle());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void changeEditMode(int mode) {
        if (mode == 1) {
            loadUserInfoValues();

            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText userValue : mUserInfo) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);
            }
        } else {
            saveUserInfoValues();
            mFab.setImageResource(R.drawable.ic_edit_black_24dp);
            for (EditText userValue : mUserInfo) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
            }
        }


    }

    private void loadUserInfoValues() {
        List<String> userData = mDataManager.getPreferenceManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfo.get(i).setText(userData.get(i));
        }


    }

    private void saveUserInfoValues() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfo) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferenceManager().saveUserProfileData(userData);

    }


}
