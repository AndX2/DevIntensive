package com.softdesign.devintensive.ui.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.net.response.UserListRes;
import com.softdesign.devintensive.ui.adapters.UserListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by savos on 16.07.2016.
 */

public class UserListActivity extends BaseActivity {

    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private RecyclerView mRecyclerView;
    private UserListAdapter mUserListAdapter;

    private DataManager mDataManager;

    private ArrayList<UserListRes> mUsers;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list_activity);

        mRecyclerView = (RecyclerView) findViewById(R.id.user_list);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.list_coordinator_layout);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.list_drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.list_toolbar);

        setupToolbar();
        setupDrawer();
        
        loadUsers();


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

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }


    private void loadUsers() {
        Call<ArrayList<UserListRes>> call = DataManager.getInstance().getNetworkManager().getUserList();
        call.enqueue(new Callback<ArrayList<UserListRes>>() {
            @Override
            public void onResponse(Call<ArrayList<UserListRes>> call, Response<ArrayList<UserListRes>> response) {
                if (response.code() == 200) {
                    mUsers = response.body();
                    mUserListAdapter = new UserListAdapter(mUsers);
                    mRecyclerView.setAdapter(mUserListAdapter);
                }else showSnackbar("wrong response: " + response.code());
            }

            @Override
            public void onFailure(Call<ArrayList<UserListRes>> call, Throwable t) {

            }


        });
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void setupDrawer() {
       
    }
}
