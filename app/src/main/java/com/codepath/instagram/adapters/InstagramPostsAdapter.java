package com.codepath.instagram.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramPost;

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

        holder.tvUserName.setText(post.user.userName);
    }

    @Override
    public int getItemCount() { return postList.size(); }

    public static class InstagramPostViewHolder extends RecyclerView.ViewHolder {
        //public ImageView sdvUserProfileImage;
        public TextView tvUserName;

        public InstagramPostViewHolder(View layoutView) {
            super(layoutView);
            //  sdvUserProfileImage = (ImageView) layoutView.findViewById(R.id.sdvUserProfileImage);
            tvUserName = (TextView) layoutView.findViewById(R.id.tvUserName);
        }
    }
}
