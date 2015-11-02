package com.codepath.instagram.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramCommentsAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by dleroy on 10/28/15.
 */
public class CommentsActivity extends AppCompatActivity {
    private RecyclerView rvComments;
    private ArrayList<InstagramComment> comments;
    private InstagramCommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String mediaId = getIntent().getStringExtra("mediaId");

        comments = new ArrayList<InstagramComment>();
        rvComments = (RecyclerView) findViewById(R.id.rvComments);
        RecyclerView.ItemDecoration itemDecoration = new SimpleVerticalSpacerItemDecoration(16);
        rvComments.addItemDecoration(itemDecoration);
        adapter = new InstagramCommentsAdapter(comments);
        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(this));

        MainApplication.getRestClient().getComments(mediaId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                comments.clear();
                comments.addAll(Utils.decodeCommentsFromJsonResponse(response));
                adapter.notifyDataSetChanged();

                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showNetworkFailureAlert();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void showNetworkFailureAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Network Error")
                .setMessage("A network error has occurred")
                .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
