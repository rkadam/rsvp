package com.pandora.rsvp.ui;

import com.pandora.rsvp.R;
import com.pandora.rsvp.service.contract.Invitation;
import com.pandora.rsvp.ui.adapter.InvitationResponsesPagerAdapter;
import com.pandora.rsvp.ui.base.BaseActivity;

import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;

import butterknife.Bind;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class InvitationResponsesActivity extends BaseActivity {

    public static final String INVITATION_KEY = "InvitationKey";
    @Bind(R.id.view_pager)
    ViewPager pager;
    @Bind(R.id.pager_tab_strip)
    PagerTabStrip strip;

    @Override
    protected int getActivityLayoutRes() {
        return R.layout.activity_invitation_responses_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Invitation invitation = bundle.getParcelable(INVITATION_KEY);
            pager.setAdapter(new InvitationResponsesPagerAdapter(invitation));
        } else {
            finish();
        }
    }
}
