package com.codepath.instagram.networking;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;

import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramPosts;
import com.codepath.instagram.persistence.InstagramClientDatabase;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by dleroy on 11/3/15.
 */
public class InstagramService extends IntentService {
    InstagramClientDatabase instagramClientDatabase;
    Context context;
    public static final String ACTION = "com.codepath.instagram.networking.InstagramService";

    public InstagramService() {
        super(ACTION);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        context = MainApplication.getContext();
        final InstagramPosts serialPosts = new InstagramPosts();
        instagramClientDatabase = InstagramClientDatabase.getInstance(context);

        if (!isNetworkAvailable()) {
            serialPosts.posts = instagramClientDatabase.getAllInstagramPosts();
            sendPosts(serialPosts);
        } else {
            SyncHttpClient syncHttpClient = new SyncHttpClient();
            InstagramClient client = MainApplication.getRestClient();
            RequestParams params = new RequestParams("access_token", client.checkAccessToken().getToken());
            syncHttpClient.get(this, InstagramClient.REST_URL + "media/popular",//"users/self/feed",
                    params, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess ( int statusCode, Header[] headers, JSONObject response){
                    List<InstagramPost> postList = Utils.decodePostsFromJsonResponse(response);
                    serialPosts.posts = postList;

                    instagramClientDatabase.emptyAllTables();
                    instagramClientDatabase.addInstagramPosts(postList);
                    sendPosts(serialPosts);
                }

                @Override
                public void onFailure ( int statusCode, Header[] headers, String
                responseString, Throwable throwable){
                    serialPosts.posts = instagramClientDatabase.getAllInstagramPosts();
                    sendPosts(serialPosts);
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }
    }
    private void sendPosts(InstagramPosts serialPosts) {
        Intent i = new Intent(ACTION);
        i.putExtra("serialPosts", serialPosts);
        LocalBroadcastManager.getInstance(InstagramService.this).sendBroadcast(i);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
