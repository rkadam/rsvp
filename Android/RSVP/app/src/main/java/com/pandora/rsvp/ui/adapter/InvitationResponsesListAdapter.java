package com.pandora.rsvp.ui.adapter;

import com.pandora.rsvp.R;
import com.pandora.rsvp.app.RSVPApp;
import com.squareup.picasso.Picasso;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class InvitationResponsesListAdapter extends RecyclerView.Adapter<InvitationResponsesListAdapter.InvitationResponseItemViewHolder> {

    @Inject
    Picasso mPicasso;

    private int count;

    public InvitationResponsesListAdapter(int count) {
        this.count = count;
        RSVPApp.getComponent().inject(this);
    }

    @Override
    public InvitationResponseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InvitationResponseItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.invitation_respondent_item, parent, false));
    }

    @Override
    public void onBindViewHolder(InvitationResponseItemViewHolder holder, int position) {
        holder.name.setText("Random name no" + (position + 1));
        holder.title.setText("Software Engineer");
        mPicasso.load(R.drawable.ic_people).into(holder.profilePic);
    }

    @Override
    public int getItemCount() {
        return count;
    }

    static class InvitationResponseItemViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView title;
        CircleImageView profilePic;

        public InvitationResponseItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.user_name);
            title = (TextView) itemView.findViewById(R.id.user_title);
            profilePic = (CircleImageView) itemView.findViewById(R.id.profile_image);

        }
    }
}
