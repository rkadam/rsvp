package com.pandora.rsvp.ui.adapter;

import com.pandora.rsvp.R;
import com.pandora.rsvp.app.RSVPApp;
import com.pandora.rsvp.service.contract.InviteResponse;
import com.squareup.picasso.Picasso;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class InvitationResponsesListAdapter extends RecyclerView.Adapter<InvitationResponsesListAdapter.InvitationResponseItemViewHolder> {

    @Inject
    Picasso mPicasso;
    List<InviteResponse> mInviteResponses;

    public InvitationResponsesListAdapter(List<InviteResponse> inviteResponses) {
        RSVPApp.getComponent().inject(this);
        this.mInviteResponses = inviteResponses;
    }

    @Override
    public InvitationResponseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InvitationResponseItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.invitation_respondent_item, parent, false));
    }

    @Override
    public void onBindViewHolder(InvitationResponseItemViewHolder holder, int position) {
        InviteResponse response = mInviteResponses.get(position);
        holder.name.setText(response.name);
        holder.title.setText(response.department);
        holder.body.setText(response.response_body);
        String imageUrl = String.format("https://ray.savagebeast.com/sbldap/image.cgi?uid=%s", response.uid);//Todo: Extract this for reuse.
        mPicasso.load(imageUrl).into(holder.profilePic);
    }

    @Override
    public int getItemCount() {
        return mInviteResponses.size();
    }

    static class InvitationResponseItemViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView title;
        TextView body;
        CircleImageView profilePic;

        public InvitationResponseItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.user_name);
            title = (TextView) itemView.findViewById(R.id.user_title);
            profilePic = (CircleImageView) itemView.findViewById(R.id.profile_image);
            body = (TextView) itemView.findViewById(R.id.user_response);

        }
    }
}
