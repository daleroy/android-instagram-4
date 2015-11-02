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
import com.codepath.instagram.adapters.InstagramTagsAdapter;
import com.codepath.instagram.helpers.DividerItemDecoration;
import com.codepath.instagram.models.InstagramSearchTag;

import java.util.ArrayList;

/**
 * Created by dleroy on 11/1/15.
 */
public class SearchTagsFragment extends Fragment{
    private RecyclerView rvTagSearchResults;
    private ArrayList<InstagramSearchTag> tags;
    private InstagramTagsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tags = new ArrayList<InstagramSearchTag>();

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_search, container, false);

        rvTagSearchResults = (RecyclerView) view.findViewById(R.id.rvTagSearchResults);
        adapter = new InstagramTagsAdapter(tags);
        rvTagSearchResults.setAdapter(adapter);
        rvTagSearchResults.setLayoutManager(new LinearLayoutManager(null));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(container.getContext(), DividerItemDecoration.VERTICAL_LIST);
        rvTagSearchResults.addItemDecoration(itemDecoration);

        return view;
    }

    public static SearchTagsFragment newInstance() {
        SearchTagsFragment fragment = new SearchTagsFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onTagsLoaded(ArrayList<InstagramSearchTag> tags) {
        this.tags.clear();
        this.tags.addAll(tags);
        adapter.notifyDataSetChanged();
    }
}
