package com.codepath.instagram.networking;

import android.content.Context;

import com.codepath.instagram.helpers.Constants;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by dleroy on 10/28/15.
 */
public class InstagramClient extends OAuthBaseClient {
    public static final Class REST_API_CLASS = InstagramApi.class;
    public static final String REST_URL = "https://api.instagram.com/v1/";
    //public static final String REST_CONSUMER_KEY = "ccf1f9fa473a460282f5c23425e6aca6";
    //public static final String REST_CONSUMER_SECRET = "ed9a21636bcb4e3d927f1843fd9c7542";
    public static final String REST_CALLBACK_URL = Constants.REDIRECT_URI;
    public static final String SCOPE = Constants.SCOPE;
    public static final String REST_CONSUMER_KEY = "7f5321002cc04089b778e463cd87953f";
    public static final String REST_CONSUMER_SECRET = "a9980e6933814fd3848dba9f6b370b63";
    //private static final String CLIENT_ID = "ccf1f9fa473a460282f5c23425e6aca6";
    //private static final String API_BASE_URL = "https://api.instagram.com/v1/";
    //private OAuthAsyncHttpClient client;

    private static String getUrl(String relativeUrl) {
        return REST_URL + relativeUrl;
    }
    public InstagramClient(Context context) {
        super(context, REST_API_CLASS, REST_URL,
                REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL, SCOPE);
    }

    public void getPopularFeed(JsonHttpResponseHandler responseHandler) {
        String url = getUrl("media/popular?client_id=" + REST_CONSUMER_KEY);
        RequestParams params = new RequestParams();
        params.add("access_token", client.getAccessToken().getToken());
        client.get(url, params, responseHandler);
    }

    public void getComments(String mediaId, JsonHttpResponseHandler responseHandler) {
        String url = getUrl("media/" + mediaId + "/comments?client_id=" + REST_CONSUMER_KEY);
        RequestParams params = new RequestParams();
        params.add("access_token", client.getAccessToken().getToken());
        client.get(url, params, responseHandler);
    }
    public void getOwnFeed(JsonHttpResponseHandler responseHandler) {
        String url = getUrl("users/self/feed");
        RequestParams params = new RequestParams();
        params.add("access_token", client.getAccessToken().getToken());
        client.get(url, params, responseHandler);
    }

    public void searchUsersByName(String name, JsonHttpResponseHandler responseHandler) {
        String url = getUrl("users/search?q=" + name);
        RequestParams params = new RequestParams();
        params.add("access_token", client.getAccessToken().getToken());
        client.get(url, params, responseHandler);
    }

    public void searchTagsByKeyword(String keyword, JsonHttpResponseHandler responseHandler) {
        String url = getUrl("tags/search?q=" + keyword);
        RequestParams params = new RequestParams();
        params.add("access_token", client.getAccessToken().getToken());
        client.get(url, params, responseHandler);
    }

}
