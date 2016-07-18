package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;

import java.util.List;

/**
 * Created by savos on 16.07.2016.
 */

public class RepoAdapter extends BaseAdapter {

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
    public View getView(int i, View view, ViewGroup viewGroup) {

        View itemView = view;
        if (itemView == null){
            itemView = mInflater.inflate(R.layout.item_repo, viewGroup, false);
        }
        EditText repoName = (EditText) itemView.findViewById(R.id.et_item_git);
        repoName.setText(mRepositories.get(i));
        return itemView;
    }
}
