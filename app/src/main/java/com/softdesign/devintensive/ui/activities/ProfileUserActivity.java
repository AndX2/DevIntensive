package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

import com.softdesign.devintensive.DevIntensiveApplication;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepoAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;



public class ProfileUserActivity extends BaseActivity {
    private final String TAG = ConstantManager.TAG_PROFILE_USER;

    private Toolbar mToolbar;
    private ImageView profilePhoto;
    private EditText mUserBio;
    TextView mRait, mCodeLines, mProjects;
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    ListView mRepoListView;

    UserDTO userDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        profilePhoto = (ImageView)findViewById(R.id.user_profile_placeholder_image);
        mUserBio = (EditText)findViewById(R.id.et_item_bio);
        mRait = (TextView) findViewById(R.id.tv_ratio);
        mCodeLines = (TextView) findViewById(R.id.tv_code_rows);
        mProjects = (TextView) findViewById(R.id.tv_projects);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


        mRepoListView = (ListView)findViewById(R.id.repo_list);
        setupToolbar();

        initProfileUser();

    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initProfileUser() {
        userDto = getIntent().getParcelableExtra(ConstantManager.PARCEL_USER_KEY);

        final List<String> repositories = userDto.getRepositories();
        RepoAdapter repoAdapter = new RepoAdapter(repositories, this);
        mRepoListView.setAdapter(repoAdapter);
        setListGitHeight(mRepoListView);
        mRait.setText(userDto.getRating());
        mCodeLines.setText(userDto.getCodeLines());
        mProjects.setText(userDto.getProjects());
        mUserBio.setText(userDto.getBio());
        mCollapsingToolbarLayout.setTitle(userDto.getFullName());

        final Picasso picasso = DataManager.getInstance().getNetworkManager().getPicasso();
        final Drawable stubPhoto = DevIntensiveApplication.getAppContext().getResources().getDrawable(R.drawable.user_bg);
        try {
            picasso.with(this)
                    .load(userDto.getPhoto())
                    .placeholder(stubPhoto)
                    .fit()
                    .centerCrop()
                    .error(stubPhoto)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(profilePhoto, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            picasso.with(ProfileUserActivity.this)
                                    .load(userDto.getPhoto())
                                    .placeholder(stubPhoto)
                                    .fit()
                                    .centerCrop()
                                    .error(stubPhoto)
                                    .into(profilePhoto, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Log.d(TAG, "impossible load photo for user: " + userDto.getFullName());
                                        }
                                    });

                        }
                    });
        }catch (Exception e){
            Log.d(TAG, "bad photo link user: " + userDto.getFullName());
        }
//        Picasso.with(this)
//                .load(userDto.getPhoto())
//                .error(R.drawable.user_bg)
//                .placeholder(R.drawable.user_bg)
//                .into(profilePhoto);
    }

    private static void setListGitHeight(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        View view = listAdapter.getView(0, null, listView);

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );

        int totalHeight = view.getMeasuredHeight() * listAdapter.getCount();

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
