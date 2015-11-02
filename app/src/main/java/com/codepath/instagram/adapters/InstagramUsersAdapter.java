package com.codepath.instagram.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramUser;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by dleroy on 11/1/15.
 */
public class InstagramUsersAdapter extends RecyclerView.Adapter<InstagramUsersAdapter.UsersViewHolder> {
    private List<InstagramUser> users;
    private Context context;

    public InstagramUsersAdapter(List<InstagramUser> users) {
        this.users = users;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_user, parent, false);
        UsersViewHolder viewHolder = new UsersViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, int position) {
        InstagramUser user = users.get(position);

        holder.tvSearchFullName.setText(user.fullName);

        holder.tvSearchUserName.setText(user.userName);

        holder.sdvUserProfilePicture.setAspectRatio(1.0f);
        holder.sdvUserProfilePicture.setImageURI(Uri.parse(user.profilePictureUrl));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSearchUserName;
        public TextView tvSearchFullName;
        public SimpleDraweeView sdvUserProfilePicture;

        public UsersViewHolder(View layoutView) {
            super(layoutView);

            tvSearchUserName = (TextView) layoutView.findViewById(R.id.tvSearchUserName);
            tvSearchFullName = (TextView) layoutView.findViewById(R.id.tvSearchFullName);
            sdvUserProfilePicture = (SimpleDraweeView) layoutView.findViewById(R.id.sdvUserProfilePicture);
        }
    }
}
