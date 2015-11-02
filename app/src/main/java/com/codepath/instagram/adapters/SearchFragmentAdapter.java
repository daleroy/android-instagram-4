package com.codepath.instagram.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.instagram.fragments.SearchTagsFragment;
import com.codepath.instagram.fragments.SearchUsersFragment;
import com.codepath.instagram.helpers.SmartFragmentStatePagerAdapter;

/**
 * Created by dleroy on 11/1/15.
 */
public class SearchFragmentAdapter extends SmartFragmentStatePagerAdapter {
    private CharSequence[] pageTitles = {
            "Users",
            "Tags"
    };

    public SearchFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SearchUsersFragment.newInstance();
            case 1:
                return SearchTagsFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles[position];
    }
}
