package com.pandora.rsvp.ui.adapter;

import com.pandora.rsvp.R;
import com.pandora.rsvp.service.contract.Invitation;
import com.pandora.rsvp.service.contract.InviteResponse;
import com.pandora.rsvp.ui.custom.YearsChartView;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class InvitationResponsesPagerAdapter extends PagerAdapter {

    private boolean hasResponses;
    private boolean hasChosen;
    List<InviteResponse> chosenInviteResponse;
    List<InviteResponse> responsesInviteResponse;

    public InvitationResponsesPagerAdapter(Invitation invitation) {
        this.chosenInviteResponse = new ArrayList<>();
        this.responsesInviteResponse = new ArrayList<>();
        if (invitation != null) {
            for (InviteResponse response : invitation.responses) {
                responsesInviteResponse.add(response);
                if (response.selected) {
                    chosenInviteResponse.add(response);
                }
            }
        }
        this.hasResponses = responsesInviteResponse.size() > 0;
        this.hasChosen = chosenInviteResponse.size() > 0;
    }

    @Override
    public int getCount() {
        return chosenInviteResponse.size() == 0 ? 1 + (hasResponses ? 1 : 0) : 3;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        boolean chosen = hasChosen && position == 0;
        int size = chosen ? chosenInviteResponse.size() : responsesInviteResponse.size();
        if ((hasChosen && position == 2) || (!hasChosen && position == 1)) {
            YearsChartView chartView = new YearsChartView(container.getContext());
            container.addView(chartView);
            chartView.setInvitationData(responsesInviteResponse);
            return chartView;
        } else if (size > 0) {
            RecyclerView rv = new RecyclerView(container.getContext());
            rv.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            rv.setLayoutManager(new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false));
            rv.setAdapter(new InvitationResponsesListAdapter(chosen ? chosenInviteResponse : responsesInviteResponse));
            container.addView(rv);
            return rv;
        } else {
            TextView textView = new TextView(container.getContext());
            textView.setText(R.string.no_resp);
            textView.setTextColor(ContextCompat.getColor(container.getContext(), android.R.color.darker_gray));
            textView.setPadding(30, 30, 30, 0);
            container.addView(textView);
            return textView;
        } 
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String count = String.valueOf(hasChosen && position == 0 ? chosenInviteResponse.size() : responsesInviteResponse.size());
        boolean isChart = (hasChosen && position == 2) || (!hasChosen && position == 1);
        return isChart ? "Stats" : String.format(hasChosen && position == 0 ? "Chosen (%s)" : "Responded (%s)", count);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
