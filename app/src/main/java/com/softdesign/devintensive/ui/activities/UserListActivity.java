package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.net.response.UserListRes;
import com.softdesign.devintensive.ui.adapters.UserListAdapter;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by savos on 16.07.2016.
 */

public class UserListActivity extends BaseActivity {
    private final String TAG = ConstantManager.TAG_LIST_USER;

    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private RecyclerView mRecyclerView;
    private List<User> mUserList;
    private MenuItem mSearchItem;
    private UserListAdapter mUserListAdapter;
    private Handler mHandler;
    NavigationView mNavigationView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list_activity);

        mHandler = new Handler();

        mRecyclerView = (RecyclerView) findViewById(R.id.user_list);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.list_coordinator_layout);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.list_drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.list_toolbar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mNavigationView = (NavigationView)findViewById(R.id.list_navigation_view);

        setupToolbar();
        setupDrawer();

        loadUsersFromDb();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        mSearchItem = menu.findItem(R.id.search_action);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        searchView.setQueryHint(getString(R.string.type_user_name));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String s) {
                showUsersByQuery(s, 0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                showUsersByQuery(s, ConstantManager.SEARCH_LATENCY);
                return false;
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    private void showUsersByQuery(final String query, long latency){
        final String mQuery = query;
        Runnable searchUsers = new Runnable() {
            @Override
            public void run() {
                showUsers(DataManager.getInstance().getStorageManager().getUserListByName(query));
            }
        };

        mHandler.removeCallbacks(searchUsers);
        mHandler.postDelayed(searchUsers, latency);
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

    private void showUsers(List<User> users){
        mUserList = users;
        mUserListAdapter = new UserListAdapter(mUserList,
                new UserListAdapter.UserListViewHolder.CustomClickListener() {

                    @Override
                    public void onUserItemClickListener(int position) {
                        Log.d(TAG, "user picked pos: " + position);
                        UserDTO userDto = new UserDTO(mUserList.get(position));

                        Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                        profileIntent.putExtra(ConstantManager.PARCEL_USER_KEY, userDto);
                        startActivity(profileIntent);
                    }
                });
        mRecyclerView.swapAdapter(mUserListAdapter, false);

    }



    private void loadUsersFromDb(){
        try {
            mUserList = DataManager.getInstance().getStorageManager().getUserListFromDb();
            Log.d(TAG, "loadUsersFromDb count = " + mUserList.size());
            showUsers(mUserList);
        }catch (Exception e){
            Log.d(TAG, "Error loadUsersFromDb");
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
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.user_profile_menu:
                        startActivity(new Intent(UserListActivity.this, MainActivity.class));
                        break;
                    case R.id.team_menu:
                        break;
                }
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });

    }
}
