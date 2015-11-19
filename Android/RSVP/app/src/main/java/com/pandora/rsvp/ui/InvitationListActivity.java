package com.pandora.rsvp.ui;

import com.pandora.rsvp.R;
import com.pandora.rsvp.app.dagger.RSVPComponent;
import com.pandora.rsvp.service.ApiCallBack;
import com.pandora.rsvp.service.IRSVPApi;
import com.pandora.rsvp.service.contract.Invitation;
import com.pandora.rsvp.service.contract.UserInvitationsResponse;
import com.pandora.rsvp.ui.adapter.InvitationListAdapter;
import com.pandora.rsvp.ui.base.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class InvitationListActivity extends BaseActivity implements ApiCallBack<UserInvitationsResponse>, InvitationListAdapter.InvitationDetailsListener {

    @Bind(R.id.invitation_list)
    RecyclerView mInvitationList;
    @Bind(R.id.create_invitation_fab)
    FloatingActionButton mCreateInvitationFab;
    @Bind(R.id.progress)
    ProgressBar pbLoadInvitations;

    @Inject
    IRSVPApi mIRSVPApi;

    @Override
    protected int getActivityLayoutRes() {
        return R.layout.activity_invitation_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInvitationList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        
        mCreateInvitationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InvitationListActivity.this, CreateInvitationActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInvitations();
    }

    private void getInvitations() {
        mIRSVPApi.getInvitations(this);
        toggleProgress(true);
    }

    private void toggleProgress(boolean loading) {
        pbLoadInvitations.setVisibility(loading ? View.VISIBLE : View.GONE);
        mInvitationList.setVisibility(loading ? View.INVISIBLE : View.VISIBLE);
        mCreateInvitationFab.setVisibility(loading ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void performInjection(RSVPComponent component) {
        component.inject(this);
    }

    @Override
    public void onSuccess(UserInvitationsResponse successResponse) {
        mInvitationList.setAdapter(new InvitationListAdapter(successResponse, this));
        toggleProgress(false);
    }

    @Override
    public void onFailure(Throwable error) {
        snackMessage(R.string.invite_failure);
    }

    @Override
    public void onInviteClicked(Invitation invitation) {
        Intent intent = new Intent(this, InvitationResponsesActivity.class);
        intent.putExtra(InvitationResponsesActivity.INVITATION_KEY, invitation);
        startActivity(intent);
    }
}
