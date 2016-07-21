package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.redmadrobot.chronos.ChronosConnector;
import com.softdesign.devintensive.DevIntensiveApplication;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.chronos.ChronosLoadUsersFromDbByName;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.ItemTouchHelperCallback;
import com.softdesign.devintensive.ui.adapters.UserListAdapter;
import com.softdesign.devintensive.ui.customview.RoundImageView;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by savos on 16.07.2016.
 */

public class UserListActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<List<User>> {
    private final String TAG = ConstantManager.TAG_LIST_USER;
    private final int ID_DB_LOADER = 1917;

    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private RecyclerView mRecyclerView;
    private List<User> mUserList;
    private MenuItem mSearchItem;
    private UserListAdapter mUserListAdapter;
    private Handler mHandler;
    NavigationView mNavigationView;
    private Loader<List<User>> mDbLoader;
    private ChronosConnector mConnector;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnector = new ChronosConnector();
        mConnector.onCreate(this, savedInstanceState);
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

        mDbLoader = getSupportLoaderManager().initLoader(ID_DB_LOADER, null, this);
        mDbLoader.forceLoad();

        //loadUsersFromDb();
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
                mConnector.runOperation(new ChronosLoadUsersFromDbByName(query), false);
                //showUsers(DataManager.getInstance().getStorageManager().getUserListByName(query));
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

    @Override
    protected void onResume() {
        super.onResume();
        mConnector.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mConnector.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        mConnector.onPause();
        super.onPause();
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
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mUserListAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

    }

    //TODO: replace action DbLoader
//    private void loadUsersFromDb(){
//        try {
//            mUserList = DataManager.getInstance().getStorageManager().getUserListFromDb();
//            Log.d(TAG, "loadUsersFromDb count = " + mUserList.size());
//            showUsers(mUserList);
//        }catch (Exception e){
//            Log.d(TAG, "Error loadUsersFromDb");
//        }
//    }


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
        final RoundImageView imgAvatar = (RoundImageView)findViewById(R.id.iv_avatar);
        final Picasso picasso = DataManager.getInstance().getNetworkManager().getPicasso();
        final Drawable stubPhoto = DevIntensiveApplication.getAppContext().getResources().getDrawable(R.drawable.user_bg);
        final String avatar = DataManager.getInstance().getPreferenceManager().loadUserProfile().getData().getUser().getPublicInfo().getAvatar();
        try {
            picasso.with(this)
                    .load(avatar)
                    .placeholder(stubPhoto)
                    .fit()
                    .centerCrop()
                    .error(stubPhoto)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(imgAvatar, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            picasso.with(UserListActivity.this)
                                    .load(avatar)
                                    .placeholder(stubPhoto)
                                    .fit()
                                    .centerCrop()
                                    .error(stubPhoto)
                                    .into(imgAvatar, new com.squareup.picasso.Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Log.d(TAG, "impossible load avatar for user: " +
                                                    DataManager.getInstance().getPreferenceManager()
                                                            .loadUserProfile().getData().getUser().getSecondName());
                                        }
                                    });

                        }
                    });
        }catch (Exception e){
            Log.d(TAG, "bad avatar link user: " + avatar);
        }

    }


    //region=================LoaderManager.LoaderCallbacks========

    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<List<User>>(this) {
            @Override
            public List<User> loadInBackground() {
                Log.d(TAG, "loading data from DB in thread: " + Thread.currentThread().getName());
                return DataManager.getInstance().getStorageManager().getUserListFromDb();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> data) {
        showUsers(data);

    }

    @Override
    public void onLoaderReset(Loader<List<User>> loader) {

    }


    //endregion
}
