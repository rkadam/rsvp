package com.pandora.rsvp.service;

import com.pandora.rsvp.service.contract.AuthResponse;
import com.pandora.rsvp.service.contract.UserInvitationsResponse;

import java.util.List;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public interface IRSVPApi {

    void authenticate(String email, String password, ApiCallBack<AuthResponse> authCallback);

    void logout(ApiCallBack<AuthResponse> authCallBack);

    void getInvitations(ApiCallBack<UserInvitationsResponse> invitationCallBack);
    
    

}
