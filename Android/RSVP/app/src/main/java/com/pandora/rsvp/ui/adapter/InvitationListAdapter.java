package com.pandora.rsvp.ui.adapter;

import com.pandora.rsvp.R;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class InvitationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.item_invitation, parent, false);
        final InvitationItemViewHolder invitationItemViewHolder = new InvitationItemViewHolder(view);
        
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Navigate to InvitationDetailsActivity
            }
        });
        return invitationItemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    static class InvitationItemViewHolder extends RecyclerView.ViewHolder {

        public InvitationItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
