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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.DeviceDimensionsHelper;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by dleroy on 10/26/15.
 */
public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.InstagramPostViewHolder> {
    private List<InstagramPost> postList;
    private Context context;

    public InstagramPostsAdapter(List<InstagramPost> postList) { this.postList = postList; }


    @Override
    public InstagramPostsAdapter.InstagramPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(this.context);

        View itemView = inflater.inflate(R.layout.layout_item_post, parent, false);

        InstagramPostViewHolder viewHolder = new InstagramPostViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InstagramPostsAdapter.InstagramPostViewHolder holder, int position) {
        InstagramPost post = postList.get(position);
        int displayWidth = DeviceDimensionsHelper.getDisplayWidth(this.context);
        int displayHeight = DeviceDimensionsHelper.getDisplayHeight(this.context);
        Uri profileImageUri = Uri.parse(post.user.profilePictureUrl);
        Uri postImageUri = Uri.parse(post.image.imageUrl);
        String formatedDate = (String) DateUtils.getRelativeTimeSpanString(post.createdTime *1000);
        SpannableStringBuilder caption = getFormattedCaption(post.user.userName, post.caption);
        String likesCount = getFormattedLikesCount(post.likesCount);
        String commentsCount = getCommentsCountString(post.commentsCount);
        List<InstagramComment> comments = null;
        View itemCommentView = LayoutInflater.from(context).inflate(R.layout.layout_item_text_comment, holder.llComments, false);
        TextView tvComment = (TextView) itemCommentView.findViewById(R.id.tvComment);

        holder.tvUserName.setText(post.user.userName);
        holder.sdvUserProfileImage.setImageURI(profileImageUri);
        holder.tvPostDate.setText(formatedDate);
        holder.sdvPostImage.setImageURI(postImageUri);
        holder.sdvPostImage.setAspectRatio(1.0f);
        holder.sdvPostImage.setMinimumHeight(displayWidth);
        holder.tvLikeCount.setText(likesCount);
        holder.tvCaption.setText(caption);

        holder.llComments.removeAllViews();

        if (post.comments.size() >= 2) {
            comments = post.comments.subList(0, 2);
            tvComment.setText(commentsCount);
            holder.llComments.addView(itemCommentView);
        } else if (post.comments.size() == 1) {
            comments = post.comments.subList(0,1);
        }

        if (comments != null) for (InstagramComment comment : comments) {
            itemCommentView = LayoutInflater.from(context).inflate(R.layout.layout_item_text_comment, holder.llComments, false);
            tvComment = (TextView) itemCommentView.findViewById(R.id.tvComment);
            SpannableStringBuilder formattedComment = getFormattedCaption(comment.user.userName, comment.text);
            tvComment.setText(formattedComment);
            holder.llComments.addView(itemCommentView);
        }

    }

    @Override
    public int getItemCount() { return postList.size(); }

    public static class InstagramPostViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView sdvUserProfileImage;
        public SimpleDraweeView sdvPostImage;
        public TextView tvUserName;
        public TextView tvPostDate;
        public TextView tvLikeCount;
        public TextView tvCaption;
        public LinearLayout llComments;

        public InstagramPostViewHolder(View layoutView) {
            super(layoutView);
            sdvUserProfileImage = (SimpleDraweeView) layoutView.findViewById(R.id.sdvUserProfileImage);
            sdvPostImage = (SimpleDraweeView) layoutView.findViewById(R.id.sdvPostImage);
            tvUserName = (TextView) layoutView.findViewById(R.id.tvUserName);
            tvPostDate = (TextView) layoutView.findViewById(R.id.tvPostDate);
            tvLikeCount = (TextView) layoutView.findViewById(R.id.tvLikeCount);
            tvCaption = (TextView) layoutView.findViewById(R.id.tvCaption);
            llComments = (LinearLayout) layoutView.findViewById(R.id.llComents);
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

    public static String getFormattedLikesCount(int count) {
        return NumberFormat.getNumberInstance(Locale.US).format(count) + " likes";
    }

    public static String getCommentsCountString(int commentsCount) {
        return "View all " + String.valueOf(commentsCount) + " comments";
    }

    // This method is used to update data for adapter and notify adapter that data has changed
    public void updateList(List<InstagramPost> data) {
        postList = data;
        notifyDataSetChanged();
    }

}
