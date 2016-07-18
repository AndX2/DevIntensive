package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.softdesign.devintensive.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by savos on 16.07.2016.
 */

public class RepoAdapter extends BaseAdapter{

    private List<String> mRepositories;
    private Context mContext;
    private LayoutInflater mInflater;

    public RepoAdapter(List<String> repositories, Context context) {
        mRepositories = repositories;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mRepositories.size();
    }

    @Override
    public Object getItem(int i) {
        return mRepositories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View itemView = view;
        if (itemView == null){
            itemView = mInflater.inflate(R.layout.item_repo, viewGroup, false);
        }
        EditText repoName = (EditText) itemView.findViewById(R.id.et_item_git);
        repoName.setText(mRepositories.get(i));
        ImageView btnGit = (ImageView)itemView.findViewById(R.id.btn_item_git);
        btnGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlString = mRepositories.get(i);
                if (!urlString.contains("http")) urlString = "https://" + urlString;
                Uri address = Uri.parse(urlString);
                Intent openlink = new Intent(Intent.ACTION_VIEW, address);
                mContext.startActivity(openlink);
            }
        });
        return itemView;
    }



}
