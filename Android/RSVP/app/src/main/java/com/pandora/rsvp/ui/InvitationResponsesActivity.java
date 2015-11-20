package com.pandora.rsvp.ui;

import com.pandora.rsvp.R;
import com.pandora.rsvp.app.dagger.RSVPComponent;
import com.pandora.rsvp.service.ApiCallBack;
import com.pandora.rsvp.service.IRSVPApi;
import com.pandora.rsvp.service.contract.Invitation;
import com.pandora.rsvp.service.contract.SingeUserInvitationResponse;
import com.pandora.rsvp.ui.adapter.InvitationResponsesPagerAdapter;
import com.pandora.rsvp.ui.base.BaseActivity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class InvitationResponsesActivity extends BaseActivity implements ApiCallBack<SingeUserInvitationResponse>, WrapUpMessageDialog.SubmitWrapUpmListener {
    
    public static final String INVITATION_KEY = "InvitationKey";
    public static final String KEY_INVITATION_DESCRIPTION = "key_invitation_description";
    public static final String KEY_NUM_INVITES = "key_num_invites";
    
    @Bind(R.id.view_pager)
    ViewPager pager;
    @Bind(R.id.tabs)
    TabLayout strip;
    @Bind(R.id.responders_action_button)
    Button respondersActionButton;
    @Bind(R.id.rsvp_title)
    TextView invitationTitle;
    @Bind(R.id.rsvp_date)
    TextView invitationDate;
    @Bind(R.id.rsvp_body)
    TextView body;
    @Bind(R.id.rsvp_method)
    TextView method;

    @Bind(R.id.details_container)
    LinearLayout container;
    @Bind(R.id.progress)
    ProgressBar pbLoading;

    @Inject
    IRSVPApi api;

    private InvitationResponsesPagerAdapter adapter;
    private Invitation invitation;

    @Override
    protected int getActivityLayoutRes() {
        return R.layout.activity_invitation_responses_activity;
    }

    @Override
    protected void performInjection(RSVPComponent component) {
        component.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            invitation = bundle.getParcelable(INVITATION_KEY);
            if (invitation == null) {
                finish();
                return;
            }
            adapter = new InvitationResponsesPagerAdapter(invitation);
            pager.setAdapter(adapter);
            toggleProgress(false);
        } else {
            finish();
        }
        initLabels();
        strip.setupWithViewPager(pager);
        respondersActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        updateButton();
    }

    private void initLabels() {
        invitationTitle.setText(invitation.title);
        body.setText(invitation.invitation_body);
        method.setText(invitation.method);

        SimpleDateFormat formatter = new SimpleDateFormat("MMM d yyyy", Locale.US);
        StringBuilder builder = new StringBuilder(getResources().getString(R.string.from));
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(invitation.create_time);
        builder.append(" ")
                .append(formatter.format(cal.getTime()));
        cal.setTimeInMillis(invitation.rsvp_by_time);
        builder.append("- ").append(formatter.format(cal.getTime()));
        invitationDate.setText(builder.toString());

    }

    public void updateButton() {
        boolean isChosenTab = adapter.getCount() > 1 && pager.getCurrentItem() == 0;
        respondersActionButton.setText(getResources().getString(isChosenTab ? R.string.email_chosen_responders
                : R.string.select_winners));
        boolean enabled = (isChosenTab || invitation.responses.size() > 0) && invitation.active;
        respondersActionButton.setEnabled(enabled);
        respondersActionButton.setAlpha(enabled ? 1f : 0.5f);
    }

    private void submit() {
        int count = adapter.getCount();
        int currentItem = pager.getCurrentItem();
        if ((count > 1 && currentItem == 1) || (count == 1)) {
            toggleProgress(true);
            api.selectWinners(invitation.id, this);
        } else {
            WrapUpMessageDialog dialog = WrapUpMessageDialog.newInstance();
            dialog.setSubmitWrapUpmListener(this);
            dialog.show(getSupportFragmentManager(), dialog.getClass().getSimpleName());
        }
    }

    @Override
    public void onSuccess(SingeUserInvitationResponse successResponse) {
        if (successResponse == null || successResponse.data == null) {
            return;
        }
        invitation = successResponse.data;
        adapter = new InvitationResponsesPagerAdapter(invitation);
        pager.setAdapter(adapter);
        strip.setupWithViewPager(pager);
        toggleProgress(false);
        updateButton();

    }

    @Override
    public void onFailure(Throwable error) {
        snackMessage(R.string.error_selecting_winners);
        toggleProgress(false);
    }

    public void toggleProgress(boolean loading) {
        pbLoading.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
        container.setVisibility(loading ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void submitWrapUp(String winnerMg, String msg) {
        api.submitWrapUp(invitation.id, winnerMg, msg, this);
        toggleProgress(true);
    }
}
