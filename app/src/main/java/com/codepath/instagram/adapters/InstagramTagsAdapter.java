package com.codepath.instagram.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramSearchTag;

import java.util.ArrayList;

/**
 * Created by dleroy on 11/1/15.
 */
public class InstagramTagsAdapter extends RecyclerView.Adapter<InstagramTagsAdapter.TagsViewHolder> {
    private ArrayList<InstagramSearchTag> tags;
    private Context context;

    public InstagramTagsAdapter(ArrayList<InstagramSearchTag> tags) {
        this.tags = tags;
    }

    @Override
    public TagsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_tag, parent, false);
        TagsViewHolder viewHolder = new TagsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TagsViewHolder holder, int position) {
        InstagramSearchTag tag = tags.get(position);
        Resources res = context.getResources();

        holder.tvSearchTagName.setText("#" + tag.tag);
        holder.tvSearchTagPosts.setText("Likes " + String.valueOf(Utils.formatNumberForDisplay(tag.count)));
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public static class TagsViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSearchTagName;
        public TextView tvSearchTagPosts;

        public TagsViewHolder(View layoutView) {
            super(layoutView);

            tvSearchTagName = (TextView) layoutView.findViewById(R.id.tvSearchTagName);
            tvSearchTagPosts = (TextView) layoutView.findViewById(R.id.tvSearchTagPosts);
        }
    }
}
