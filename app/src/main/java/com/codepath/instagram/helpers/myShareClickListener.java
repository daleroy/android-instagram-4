package com.codepath.instagram.helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by dleroy on 10/29/15.
 */
public class myShareClickListener implements View.OnClickListener {
    Uri uri;
    Context mContext;

    public myShareClickListener(Uri uri, Context context) {
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
