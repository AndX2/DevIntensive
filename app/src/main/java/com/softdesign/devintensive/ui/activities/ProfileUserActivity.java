package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepoAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.List;



public class ProfileUserActivity extends BaseActivity {

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

        Picasso.with(this)
                .load(userDto.getPhoto())
                .error(R.drawable.user_bg)
                .placeholder(R.drawable.user_bg)
                .into(profilePhoto);
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
