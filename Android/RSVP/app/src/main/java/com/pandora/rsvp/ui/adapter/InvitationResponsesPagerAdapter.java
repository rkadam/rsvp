package com.pandora.rsvp.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class InvitationResponsesPagerAdapter extends PagerAdapter {
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RecyclerView rv = new RecyclerView(container.getContext());
        rv.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rv.setLayoutManager(new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(new InvitationResponsesListAdapter(position == 0 ? 4 : 40));
        container.addView(rv);
        return rv;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "Chosen (6)" : "Pending(40)";
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
