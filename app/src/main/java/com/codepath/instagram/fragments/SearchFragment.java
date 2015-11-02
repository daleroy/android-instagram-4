package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.SearchFragmentAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramSearchTag;
import com.codepath.instagram.models.InstagramUser;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by dleroy on 11/1/15.
 */
public class SearchFragment extends Fragment {
    private SearchFragmentAdapter adapterViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.vpSearchType);
        adapterViewPager = new SearchFragmentAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tlSearchType);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_users, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String name) {
                MainApplication.getRestClient().searchUsersByName(name, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        SearchUsersFragment searchUsersFragment = (SearchUsersFragment) adapterViewPager.getRegisteredFragment(0);

                        if (searchUsersFragment != null) {
                            searchUsersFragment.onUsersLoaded((ArrayList<InstagramUser>) Utils.decodeUsersFromJsonResponse(response));
                        }

                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable errorResponse) {
                        super.onFailure(statusCode, headers, response, errorResponse);
                    }
                });

                MainApplication.getRestClient().searchTagsByKeyword(name, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        SearchTagsFragment searchTagsFragment = (SearchTagsFragment) adapterViewPager.getRegisteredFragment(1);

                        if (searchTagsFragment != null) {
                            searchTagsFragment.onTagsLoaded((ArrayList<InstagramSearchTag>) Utils.decodeSearchTagsFromJsonResponse(response));
                        }

                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable errorResponse) {
                        super.onFailure(statusCode, headers, response, errorResponse);
                    }
                });

                searchItem.collapseActionView();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
}
