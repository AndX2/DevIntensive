package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.net.response.UserListRes;
import com.softdesign.devintensive.ui.adapters.UserListAdapter;
import com.softdesign.devintensive.utils.ConstantManager;

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

    private ArrayList<UserListRes.Data> mUsers;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list_activity);

        mRecyclerView = (RecyclerView) findViewById(R.id.user_list);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.list_coordinator_layout);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.list_drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.list_toolbar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

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
        Log.d("UserListTag", "loadUsers");
        Call<UserListRes> call = DataManager.getInstance().getNetworkManager().getUserList();
        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                Log.d("UserListTag", "onResponse");
                if (response.code() == 200) {
                    mUsers = response.body().getData();
                    mRecyclerView.setAdapter(new UserListAdapter(mUsers,
                            new UserListAdapter.UserListViewHolder.CustomClickListener() {

                                @Override
                                public void onUserItemClickListener(int position) {
                                    Log.d("UserListTag", "user picked pos: " + position);
                                    UserDTO userDto = new UserDTO(mUsers.get(position));

                                    Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                                    profileIntent.putExtra(ConstantManager.PARCEL_USER_KEY, userDto);
                                    startActivity(profileIntent);
                                }
                            }));
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {

            }
        });
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

    }
}
