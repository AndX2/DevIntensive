package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.softdesign.devintensive.DevIntensiveApplication;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.net.response.UserListRes;
import com.softdesign.devintensive.pojo.UserProfile;
import com.softdesign.devintensive.ui.customview.AspectRatioImageView;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by savos on 16.07.2016.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    static final String TAG = ConstantManager.TAG_USER_LIST_ADAPTER;

    public UserListAdapter(List<User> userList, UserListViewHolder.CustomClickListener clickListener) {
        mUserList = userList;
        this.mClickListener = clickListener;
    }

    Context mContext;
    private UserListViewHolder.CustomClickListener mClickListener;

    private List<User> mUserList;

    @Override
    public UserListAdapter.UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.item_user_list, parent, false);
        return new UserListViewHolder(convertView, mClickListener);
    }

    @Override
    public void onBindViewHolder(final UserListAdapter.UserListViewHolder holder, int position) {

        final Picasso picasso = DataManager.getInstance().getNetworkManager().getPicasso();
        final User user = mUserList.get(position);
        //replace bad or null link to "null" is not work. picasso.load("null") -> Fatal exception
        try {
            picasso.with(mContext)
                    .load(user.getPhoto())
                    .placeholder(holder.stubPhoto)
                    .fit()
                    .centerCrop()
                    .error(holder.stubPhoto)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(holder.imgUserPhoto, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            picasso.with(mContext)
                                    .load(user.getPhoto())
                                    .placeholder(holder.stubPhoto)
                                    .fit()
                                    .centerCrop()
                                    .error(holder.stubPhoto)
                                    .into(holder.imgUserPhoto, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Log.d(TAG, "impossible load photo for user: " + user.getFullName());
                                        }
                                    });

                        }
                    });
        }catch (Exception e){
            Log.d(TAG, "bad photo link user: " + user.getFullName());
        }

        holder.tvFullName.setText(user.getFullName());
        holder.tvRatio.setText(user.getRaiting() + "");
        holder.tvCodeLines.setText(user.getCodeLines() + "");
        holder.tvProjects.setText(user.getProjects() + "");

        if ((user.getBio() == null) ||(user.getBio() == "")){
            holder.tvBio.setVisibility(View.GONE);
        }else {
            holder.tvBio.setVisibility(View.VISIBLE);
            holder.tvBio.setText(user.getBio());

        }

    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }




    public static class UserListViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private AspectRatioImageView imgUserPhoto;
        private TextView tvFullName, tvRatio, tvCodeLines, tvProjects, tvBio;
        private Button btnViewMore;
        private Drawable stubPhoto;

        private CustomClickListener mListener;

        public UserListViewHolder(View itemView, CustomClickListener clickListener) {
            super(itemView);

            mListener = clickListener;

            stubPhoto = DevIntensiveApplication.getAppContext().getResources().getDrawable(R.drawable.user_bg);

            imgUserPhoto = (AspectRatioImageView) itemView.findViewById(R.id.img_item_user_photo);
            tvFullName= (TextView)itemView.findViewById(R.id.user_full_name_txt);
            tvRatio = (TextView)itemView.findViewById(R.id.tv_item_ratio);
            tvCodeLines = (TextView)itemView.findViewById(R.id.tv_item_code_rows);
            tvProjects = (TextView)itemView.findViewById(R.id.tv_item_projects);
            tvBio = (TextView)itemView.findViewById(R.id.tv_item_bio);
            btnViewMore = (Button) itemView.findViewById(R.id.more_info_btn);
            btnViewMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null){
                mListener.onUserItemClickListener(getAdapterPosition());
            }
        }

        public interface CustomClickListener {
            void onUserItemClickListener(int position);
        }
    }
}
