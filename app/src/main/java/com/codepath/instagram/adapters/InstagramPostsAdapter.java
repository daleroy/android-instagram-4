package com.codepath.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.CommentsActivity;
import com.codepath.instagram.helpers.DeviceDimensionsHelper;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

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
        holder.tvDots.setOnClickListener(new myShareClickListener(postImageUri, context));

        holder.llComments.removeAllViews();

        if (post.comments.size() >= 2) {
            comments = post.comments.subList(0, 2);
            tvComment.setText(commentsCount);
            tvComment.setOnClickListener(new myClickListener(post.mediaId, context));
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

    public class myClickListener implements View.OnClickListener {
        String mediaId;
        Context context;

        myClickListener(String mediaId, Context context) {
            this.mediaId = mediaId;
            this.context = context;
        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(this.context, CommentsActivity.class);
            intent.putExtra("mediaId", mediaId);

            this.context.startActivity(intent);

            //Toast.makeText(context, "mediaId" + this.mediaId, Toast.LENGTH_SHORT).show();
        }
    }

    public class myShareClickListener implements View.OnClickListener {
        Uri uri;
        Context mContext;

        myShareClickListener(Uri uri, Context context) {
            this.uri =uri;
            this.mContext = context;
        }
        @Override
        public void onClick(View view) {
            getBitmapFromUri(uri);

            //Toast.makeText(context, "mediaId" + this.mediaId, Toast.LENGTH_SHORT).show();
        }
        private void getBitmapFromUri(Uri uri) {

            ImagePipeline imagePipeline = Fresco.getImagePipeline();

            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setRequestPriority(Priority.HIGH)
                    .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                    .build();

            DataSource<CloseableReference<CloseableImage>> dataSource =
                    imagePipeline.fetchDecodedImage(imageRequest, mContext);

            try {
                dataSource.subscribe(new BaseBitmapDataSubscriber() {

                    @Override
                    public void onNewResultImpl(@Nullable Bitmap bitmap) {
                        if (bitmap == null) {
                            Log.d("BookDetailActivity", "Bitmap data source returned success, but bitmap null.");
                            return;
                        }
                        // The bitmap provided to this method is only guaranteed to be around
                        // for the lifespan of this method. The image pipeline frees the
                        // bitmap's memory after this method has completed.
                        //
                        // This is fine when passing the bitmap to a system process as
                        // Android automatically creates a copy.
                        //
                        // If you need to keep the bitmap around, look into using a
                        // BaseDataSubscriber instead of a BaseBitmapDataSubscriber.
                        shareBitmap(bitmap);
                    }

                    @Override
                    public void onFailureImpl(DataSource dataSource) {
                        // No cleanup required here
                    }
                }, CallerThreadExecutor.getInstance());
            } finally {
                if (dataSource != null) {
                    dataSource.close();
                }
            }
        }

        public void shareBitmap(Bitmap bitmap) {
            String path = MediaStore.Images.Media.insertImage(this.mContext.getContentResolver(),
                    bitmap, "Image Description", null);
            Uri bmpUri = Uri.parse(path);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");

            mContext.startActivity(Intent.createChooser(shareIntent, "Share Image"));
        }
    }

    public static class InstagramPostViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView sdvUserProfileImage;
        public SimpleDraweeView sdvPostImage;
        public TextView tvUserName;
        public TextView tvPostDate;
        public TextView tvLikeCount;
        public TextView tvCaption;
        public TextView tvDots;
        public LinearLayout llComments;
        public View view;
        public InstagramPostViewHolder(View layoutView) {
            super(layoutView);
            this.view = view;
            sdvUserProfileImage = (SimpleDraweeView) layoutView.findViewById(R.id.sdvUserProfileImage);
            sdvPostImage = (SimpleDraweeView) layoutView.findViewById(R.id.sdvPostImage);
            tvUserName = (TextView) layoutView.findViewById(R.id.tvUserName);
            tvPostDate = (TextView) layoutView.findViewById(R.id.tvPostDate);
            tvLikeCount = (TextView) layoutView.findViewById(R.id.tvLikeCount);
            tvCaption = (TextView) layoutView.findViewById(R.id.tvCaption);
            tvDots = (TextView) layoutView.findViewById(R.id.tv_more_dots);
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
