package com.codepath.instagram.networking;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by dleroy on 10/28/15.
 */
public class InstagramClient {
    private static final String CLIENT_ID = "ccf1f9fa473a460282f5c23425e6aca6";
    private static final String API_BASE_URL = "https://api.instagram.com/v1/";
    private AsyncHttpClient client;

    private static String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }
    public InstagramClient() {
        this.client = new AsyncHttpClient();
    }

    public void getPopularFeed(JsonHttpResponseHandler responseHandler) {
        String url = getApiUrl("media/popular?client_id=" + CLIENT_ID);
        client.get(url, responseHandler);
    }


}
