package com.codepath.instagram.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramComment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

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

        Uri profileImageUri = Uri.parse(comment.user.profilePictureUrl);
        String formatedDate = (String) DateUtils.getRelativeTimeSpanString(comment.createdTime *1000);

        holder.sdvUserProfileImage.setImageURI(profileImageUri);
        holder.tvPostDate.setText(formatedDate);
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

    private SpannableStringBuilder getFormattedCaption(String userName, String caption) {
        ForegroundColorSpan fgcsBlue = new ForegroundColorSpan(
                this.context.getResources().getColor(R.color.blue_text)
        );

        SpannableStringBuilder ssb = new SpannableStringBuilder(userName);

        ssb.setSpan(
                fgcsBlue,
                0,
                ssb.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        if (caption != null) {


            ssb.append(" ");

            ForegroundColorSpan fgcsGray = new ForegroundColorSpan(
                    this.context.getResources().getColor(R.color.gray_text)
            );

            ssb.append(caption);
            ssb.setSpan(
                    fgcsGray,
                    ssb.length() - caption.length(),
                    ssb.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        return ssb;
    }
    // This method is used to update data for adapter and notify adapter that data has changed
    public void updateList(List<InstagramComment> data) {
        commentList = data;
        notifyDataSetChanged();
    }
}
