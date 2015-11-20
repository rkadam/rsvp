package com.pandora.rsvp.app.dagger;

import com.pandora.rsvp.persistence.impl.UserDataManager;
import com.pandora.rsvp.service.impl.RSVPApi;
import com.pandora.rsvp.ui.CreateInvitationActivity;
import com.pandora.rsvp.ui.InvitationListActivity;
import com.pandora.rsvp.ui.InvitationResponsesActivity;
import com.pandora.rsvp.ui.LoginActivity;
import com.pandora.rsvp.ui.WrapUpMessageDialog;
import com.pandora.rsvp.ui.adapter.InvitationResponsesListAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
@Singleton
@Component(modules = {RSVPModule.class})
public interface RSVPComponent {
    void inject(LoginActivity loginActivity);

    void inject(InvitationResponsesListAdapter invitationResponsesListAdapter);

    void inject(InvitationListActivity invitationListActivity);

    void inject(InvitationResponsesActivity invitationResponsesActivity);

    void inject(CreateInvitationActivity createInvitationActivity);

    void inject(UserDataManager userDataManager);

    void inject(RSVPApi rsvpApi);
}
