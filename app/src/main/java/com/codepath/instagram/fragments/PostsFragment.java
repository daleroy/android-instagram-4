package com.codepath.instagram.fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramPostsAdapter;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramPosts;
import com.codepath.instagram.networking.InstagramService;
import com.codepath.instagram.persistence.InstagramClientDatabase;

import java.util.ArrayList;

/**
 * Created by dleroy on 11/1/15.
 */
public class PostsFragment extends Fragment {
    private ArrayList<InstagramPost> posts;
    private RecyclerView rvPosts;
    private InstagramPostsAdapter adapter;
    private Context context;
    private SwipeRefreshLayout swipeContainer;
    private InstagramClientDatabase instagramClientDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        context = container.getContext();
        instagramClientDatabase = InstagramClientDatabase.getInstance(context);
        rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);

        posts = new ArrayList<InstagramPost>();

        RecyclerView.ItemDecoration itemDecoration = new SimpleVerticalSpacerItemDecoration(24);
        rvPosts.addItemDecoration(itemDecoration);
        adapter = new InstagramPostsAdapter(posts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(context));
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPosts();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fetchPosts();

        return view;
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            InstagramPosts serialPosts = (InstagramPosts) intent.getSerializableExtra("serialPosts");

            adapter.clear();
            adapter.addAll(serialPosts.posts);
            adapter.notifyDataSetChanged();
            swipeContainer.setRefreshing(false);
        }
    };

    public void fetchPosts() {
        Intent intent = new Intent(context, InstagramService.class);
        context.startService(intent);
        if(!isNetworkAvailable()) {
            showNetworkFailureAlert();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showNetworkFailureAlert() {
        new AlertDialog.Builder(context)
                .setTitle("Network Error")
                .setMessage("A network error has occurred")
                .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(InstagramService.ACTION);
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, filter);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static PostsFragment newInstance() {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

}
