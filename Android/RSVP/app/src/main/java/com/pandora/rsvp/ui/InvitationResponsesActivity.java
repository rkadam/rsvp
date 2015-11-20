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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
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
    private static final String API_PROP_RANDOM = "random";
    private static final String API_PROP_FIRST_COME = "firstcomefirstserve";
    private static final String METHOD_RANDOM = "Random";
    private static final String METHOD_FIRST_COME = "First Come, First Serve";
    
    @Bind(R.id.view_pager)
    ViewPager pager;
    @Bind(R.id.tabs)
    TabLayout strip;
    @Bind(R.id.responders_action_button)
    Button respondersActionButton;
    @Bind(R.id.rsvp_sent_to)
    TextView emailTo;
    @Bind(R.id.rsvp_title)
    TextView invitationTitle;
    @Bind(R.id.rsvp_date)
    TextView invitationDate;
    @Bind(R.id.rsvp_body)
    TextView body;
    @Bind(R.id.rsvp_method)
    TextView method;
    @Bind(R.id.live_status)
    TextView liveStatus;

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
        String frontEndFormattedLabel = "";
        if (invitation.method != null) {
            if (invitation.method.equals(API_PROP_RANDOM)) {
                frontEndFormattedLabel = METHOD_RANDOM;
            } else if (invitation.method.equals(API_PROP_FIRST_COME)) {
                frontEndFormattedLabel = METHOD_FIRST_COME;
            } else {
                frontEndFormattedLabel = invitation.method;
            }
        }
        int accentColor = ContextCompat.getColor(this, R.color.colorAccent);
        method.setText(frontEndFormattedLabel);
        String emailToPrefix = getResources().getString(R.string.email_to) + " ";
        Spannable emailSpan = new SpannableString(emailToPrefix + invitation.email_to);
        emailSpan.setSpan(new ForegroundColorSpan(accentColor),
                0, emailToPrefix.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        emailTo.setText(emailSpan);
        SimpleDateFormat formatter = new SimpleDateFormat(CreateInvitationActivity.DATE_TIME_FORMAT, Locale.US);
        String fromPrefix = getResources().getString(R.string.from) + " ";
        StringBuilder builder = new StringBuilder(fromPrefix);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(invitation.rsvp_by_time);
        builder.append(formatter.format(cal.getTime()));
        Spannable timeSpan = new SpannableString(builder.toString());
        timeSpan.setSpan(new ForegroundColorSpan(accentColor), 0, fromPrefix.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        invitationDate.setText(timeSpan);

    }

    public void updateButton() {
        InvitationResponsesPagerAdapter.CurrentSelection selection = adapter.getCurrentSelection(pager.getCurrentItem());
        boolean enabled = (selection == InvitationResponsesPagerAdapter.CurrentSelection.CHOSEN
                || invitation.responses.size() > 0) && invitation.active;
        respondersActionButton.setText(selection == InvitationResponsesPagerAdapter.CurrentSelection.CHOSEN ?
                R.string.email_chosen_responders : R.string.select_winners);
        respondersActionButton.setEnabled(enabled);
        respondersActionButton.setAlpha(enabled ? 1f : 0.5f);
        respondersActionButton.setVisibility(invitation.active ? View.VISIBLE : View.GONE);

        liveStatus.setBackgroundColor(ContextCompat.getColor(this,
                invitation.active ? R.color.colorAccentSecondary : android.R.color.holo_red_dark));
        liveStatus.setText(invitation.active ? R.string.live : R.string.full_up);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_invite, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            api.getInvitation(invitation.id, this);
            toggleProgress(true);
        }
        return super.onOptionsItemSelected(item);
    }

    private void submit() {
        boolean hasRespondents = adapter.getRespondentCount() > 0;
        InvitationResponsesPagerAdapter.CurrentSelection selection = adapter.getCurrentSelection(pager.getCurrentItem());
        if ((selection == InvitationResponsesPagerAdapter.CurrentSelection.RESPONDENT
                || selection == InvitationResponsesPagerAdapter.CurrentSelection.CHART)
                && hasRespondents) {
            toggleProgress(true);
            api.selectWinners(invitation.id, this);
        } else if (selection == InvitationResponsesPagerAdapter.CurrentSelection.CHOSEN) {
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
