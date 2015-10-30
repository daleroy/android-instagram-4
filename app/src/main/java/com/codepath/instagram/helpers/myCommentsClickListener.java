package com.codepath.instagram.helpers;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.codepath.instagram.activities.CommentsActivity;

/**
 * Created by dleroy on 10/29/15.
 */
public class myCommentsClickListener implements View.OnClickListener {
    String mediaId;
    Context context;

    public myCommentsClickListener(String mediaId, Context context) {
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
