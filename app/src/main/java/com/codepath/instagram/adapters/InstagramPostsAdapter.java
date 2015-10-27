package com.codepath.instagram.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by dleroy on 10/26/15.
 */
public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.InstagramPostViewHolder> {
    private List<InstagramPost> postList;

    public InstagramPostsAdapter(List<InstagramPost> postList) { this.postList = postList; }


    @Override
    public InstagramPostsAdapter.InstagramPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.layout_item_post, parent, false);

        InstagramPostViewHolder viewHolder = new InstagramPostViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InstagramPostsAdapter.InstagramPostViewHolder holder, int position) {
        InstagramPost post = postList.get(position);
        Uri profileImageUri = Uri.parse(post.user.profilePictureUrl);
        Uri postImageUri = Uri.parse(post.image.imageUrl);
        String formatedDate = (String) DateUtils.getRelativeTimeSpanString(post.createdTime *1000);

        holder.tvUserName.setText(post.user.userName);
        holder.sdvUserProfileImage.setImageURI(profileImageUri);
        holder.tvPostDate.setText(formatedDate);
        holder.sdvPostImage.setImageURI(postImageUri);
        holder.tvUserNameLower.setText(post.user.userName);
        holder.tvLikeCount.setText(Integer.toString(post.likesCount));
        holder.tvCaption.setText(post.caption);
    }

    @Override
    public int getItemCount() { return postList.size(); }

    public static class InstagramPostViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView sdvUserProfileImage;
        public SimpleDraweeView sdvPostImage;
        public TextView tvUserName;
        public TextView tvPostDate;
        public TextView tvUserNameLower;
        public TextView tvLikeCount;
        public TextView tvCaption;

        public InstagramPostViewHolder(View layoutView) {
            super(layoutView);
            sdvUserProfileImage = (SimpleDraweeView) layoutView.findViewById(R.id.sdvUserProfileImage);
            sdvPostImage = (SimpleDraweeView) layoutView.findViewById(R.id.sdvPostImage);
            tvUserName = (TextView) layoutView.findViewById(R.id.tvUserName);
            tvPostDate = (TextView) layoutView.findViewById(R.id.tvPostDate);
            tvUserNameLower = (TextView) layoutView.findViewById(R.id.tvUserNameLower);
            tvLikeCount = (TextView) layoutView.findViewById(R.id.tvLikeCount);
            tvCaption = (TextView) layoutView.findViewById(R.id.tvCaption);
        }
    }
}
