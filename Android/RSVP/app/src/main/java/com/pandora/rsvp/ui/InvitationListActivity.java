package com.pandora.rsvp.ui;

import com.pandora.rsvp.R;
import com.pandora.rsvp.ui.adapter.InvitationListAdapter;
import com.pandora.rsvp.ui.base.BaseActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.Bind;

public class InvitationListActivity extends BaseActivity {

    @Bind(R.id.invitation_list)
    RecyclerView mInvitationList;
    
    @Bind(R.id.create_invitation_fab)
    FloatingActionButton mCreateInvitationFab;

    @Override
    protected int getActivityLayoutRes() {
        return R.layout.activity_invitation_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInvitationList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mInvitationList.setAdapter(new InvitationListAdapter());
        
        mCreateInvitationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Wire to CreateInvitationActivity
            }
        });
    }
}
