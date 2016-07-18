package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.net.response.UserListRes;
import com.softdesign.devintensive.ui.adapters.UserListAdapter;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

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
    private List<User> mUserList;


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

        loadUsersFromDb();


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


    private void loadUsersFromDb(){
        mUserList = DataManager.getInstance().getDaoSession().getUserDao().loadAll();
        Log.d("UserListTag", "loadUsersFromDb");
        mRecyclerView.setAdapter(new UserListAdapter(mUserList,
                new UserListAdapter.UserListViewHolder.CustomClickListener() {

                    @Override
                    public void onUserItemClickListener(int position) {
                        Log.d("UserListTag", "user picked pos: " + position);
                        UserDTO userDto = new UserDTO(mUserList.get(position));

                        Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                        profileIntent.putExtra(ConstantManager.PARCEL_USER_KEY, userDto);
                        startActivity(profileIntent);
                    }
                }));
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
