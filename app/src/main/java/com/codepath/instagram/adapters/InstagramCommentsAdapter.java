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
import com.codepath.instagram.models.InstagramComment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dleroy on 10/26/15.
 */
public class InstagramCommentsAdapter extends RecyclerView.Adapter<InstagramCommentsAdapter.InstagramCommentViewHolder> {
    private List<InstagramComment> commentList;
    private Context context;

    public InstagramCommentsAdapter(List<InstagramComment> commentList) { this.commentList = commentList; }


    @Override
    public InstagramCommentsAdapter.InstagramCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(this.context);

        View itemView = inflater.inflate(R.layout.layout_item_comment, parent, false);

        InstagramCommentViewHolder viewHolder = new InstagramCommentViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InstagramCommentsAdapter.InstagramCommentViewHolder holder, int position) {
        InstagramComment comment = commentList.get(position);
        Pattern p = Pattern.compile("(\\d*\\s\\w)");
        Uri profileImageUri = Uri.parse(comment.user.profilePictureUrl);
        String formatedDate = (String) DateUtils.getRelativeTimeSpanString(comment.createdTime *1000);
        Matcher m = p.matcher(formatedDate);
        m.find();

        holder.sdvUserProfileImage.setImageURI(profileImageUri);
        holder.tvPostDate.setText(m.group(0));
        holder.tvComment.setText(comment.text);
        holder.tvUserName.setText(comment.user.userName);
    }

    @Override
    public int getItemCount() { return commentList.size(); }

    public static class InstagramCommentViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView sdvUserProfileImage;
        public TextView tvUserName;
        public TextView tvComment;
        public TextView tvPostDate;

        public InstagramCommentViewHolder(View layoutView) {
            super(layoutView);
            sdvUserProfileImage = (SimpleDraweeView) layoutView.findViewById(R.id.sdvUserProfileImage);
            tvPostDate = (TextView) layoutView.findViewById(R.id.tvPostDate);
            tvComment = (TextView) layoutView.findViewById(R.id.tvComment);
            tvUserName = (TextView) layoutView.findViewById(R.id.tvUserName);
        }
    }

    // This method is used to update data for adapter and notify adapter that data has changed
    public void updateList(List<InstagramComment> data) {
        commentList = data;
        notifyDataSetChanged();
    }
}
