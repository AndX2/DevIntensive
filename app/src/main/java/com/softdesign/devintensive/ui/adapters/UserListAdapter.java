package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.net.response.UserListRes;
import com.softdesign.devintensive.ui.customview.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by savos on 16.07.2016.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    public UserListAdapter(List<UserListRes> userListRes) {
        mUserListRes = userListRes;
    }

    Context mContext;

    private List<UserListRes> mUserListRes;

    @Override
    public UserListAdapter.UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.item_user_list, parent, false);
        return new UserListViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(UserListAdapter.UserListViewHolder holder, int position) {

        UserListRes user = mUserListRes.get(position);
        Picasso.with(mContext)
                .load(user.getPublicInfo().getPhoto())
                .placeholder(mContext.getResources().getDrawable(R.drawable.user_bg))
                .error(mContext.getResources().getDrawable(R.drawable.user_bg))
                .into(holder.imgUserPhoto);

        holder.tvFullName.setText(user.getFullName());
        holder.tvRatio.setText(user.getProfileValues().getRait());
        holder.tvCodeLines.setText(user.getProfileValues().getLinesCode());
        holder.tvProjects.setText(user.getProfileValues().getProjects());

        if ((user.getPublicInfo().getBio() == null) ||(user.getPublicInfo().getBio() == "")){
            holder.tvBio.setVisibility(View.GONE);
        }else {
            holder.tvBio.setVisibility(View.VISIBLE);
            holder.tvBio.setText(user.getPublicInfo().getBio());

        }


    }

    @Override
    public int getItemCount() {
        return mUserListRes.size();
    }

    public static class UserListViewHolder extends RecyclerView.ViewHolder {

        private AspectRatioImageView imgUserPhoto;
        private TextView tvFullName, tvRatio, tvCodeLines, tvProjects, tvBio;
        private Button btnViewMore;

        public UserListViewHolder(View itemView) {
            super(itemView);

            imgUserPhoto = (AspectRatioImageView) itemView.findViewById(R.id.img_item_user_photo);
            tvFullName= (TextView)itemView.findViewById(R.id.user_full_name_txt);
            tvRatio = (TextView)itemView.findViewById(R.id.tv_item_ratio);
            tvCodeLines = (TextView)itemView.findViewById(R.id.tv_item_code_rows);
            tvProjects = (TextView)itemView.findViewById(R.id.tv_item_projects);
            tvBio = (TextView)itemView.findViewById(R.id.tv_item_bio);
            btnViewMore = (Button) itemView.findViewById(R.id.more_info_btn);
        }
    }
}
