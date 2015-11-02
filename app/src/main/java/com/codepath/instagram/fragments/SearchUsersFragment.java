package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramUsersAdapter;
import com.codepath.instagram.helpers.DividerItemDecoration;
import com.codepath.instagram.models.InstagramUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dleroy on 11/1/15.
 */
public class SearchUsersFragment extends Fragment {
    private List<InstagramUser> users;
    private RecyclerView rvSearchResults;
    private InstagramUsersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_search, container, false);

        rvSearchResults = (RecyclerView) view.findViewById(R.id.rvUserSearchResults);
        adapter = new InstagramUsersAdapter(users);
        rvSearchResults.setAdapter(adapter);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(container.getContext()));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(container.getContext(), DividerItemDecoration.VERTICAL_LIST);
        rvSearchResults.addItemDecoration(itemDecoration);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        users = new ArrayList<InstagramUser>();
    }


    public static SearchUsersFragment newInstance() {
        SearchUsersFragment fragment = new SearchUsersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    public void onUsersLoaded(List<InstagramUser> users) {
        this.users.clear();
        this.users.addAll(users);
        adapter.notifyDataSetChanged();
    }

}
