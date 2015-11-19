package com.pandora.rsvp.ui;

import com.pandora.rsvp.R;
import com.pandora.rsvp.service.contract.Invitation;
import com.pandora.rsvp.ui.adapter.InvitationResponsesPagerAdapter;
import com.pandora.rsvp.ui.base.BaseActivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    @Bind(R.id.responders_action_button)
    Button respondersActionButton;
    @Bind(R.id.rsvp_title)
    TextView invitationTitle;
    @Bind(R.id.rsvp_date)
    TextView invitationDate;

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
            if (invitation != null) {
                invitationTitle.setText(invitation.title);
            }
            pager.setAdapter(new InvitationResponsesPagerAdapter(invitation));
        } else {
            finish();
        }
        
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                
            }

            @Override
            public void onPageSelected(int position) {
                respondersActionButton.setText(position == 0 ? getResources().getString(R.string.email_chosen_responders) 
                        : getResources().getString(R.string.add_selected_responders));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
