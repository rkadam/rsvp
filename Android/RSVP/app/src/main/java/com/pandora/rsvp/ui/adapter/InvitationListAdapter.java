package com.pandora.rsvp.ui.adapter;

import com.pandora.rsvp.R;
import com.pandora.rsvp.service.contract.Invitation;
import com.pandora.rsvp.service.contract.InviteResponse;
import com.pandora.rsvp.service.contract.UserInvitationsResponse;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class InvitationListAdapter extends RecyclerView.Adapter<InvitationListAdapter.InvitationItemViewHolder> {

    private List<Invitation> invitationList;
    private SimpleDateFormat mSimpleDateFormat;
    private InvitationDetailsListener listener;

    public interface InvitationDetailsListener {
        void onInviteClicked(Invitation invitation);
    }

    public InvitationListAdapter(UserInvitationsResponse successResponse, InvitationDetailsListener listener) {
        this.invitationList = new ArrayList<>();
        this.listener = listener;
        mSimpleDateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        if (successResponse != null && successResponse.data != null) {
            invitationList = successResponse.data;
        }
        Collections.sort(invitationList);
    }

    @Override
    public InvitationItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.item_invitation, parent, false);
        return new InvitationItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InvitationItemViewHolder holder, int position) {
        final Invitation invitation = invitationList.get(position);
        holder.title.setText(invitation.title);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(invitation.create_time);
        holder.date.setText(mSimpleDateFormat.format(calendar.getTime()));
        holder.responseCount.setText(String.valueOf(invitation.responses != null ? invitation.responses.size() : 0));
        int chosen = 0;
        if (invitation.responses != null) {
            for (InviteResponse response : invitation.responses) {
                if (response.selected) {
                    chosen++;
                }
            }
        }
        holder.numChosen.setText(String.valueOf(chosen));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onInviteClicked(invitation);
                }
            }
        });
        holder.liveStatus.setVisibility(invitation.active ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return invitationList.size();
    }

    static class InvitationItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.invitation_title)
        TextView title;
        @Bind(R.id.invitation_date)
        TextView date;
        @Bind(R.id.num_responses)
        TextView responseCount;
        @Bind(R.id.num_chosen)
        TextView numChosen;
        @Bind(R.id.live_status)
        TextView liveStatus;
        
        public InvitationItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
