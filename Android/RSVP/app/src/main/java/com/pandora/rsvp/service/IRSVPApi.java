package com.pandora.rsvp.service;

import com.pandora.rsvp.service.contract.SimpleResponse;
import com.pandora.rsvp.service.contract.SingeUserInvitationResponse;
import com.pandora.rsvp.service.contract.UserInvitationsResponse;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public interface IRSVPApi {

    void authenticate(String email, String password, ApiCallBack<SimpleResponse> authCallback);

    void logout(ApiCallBack<SimpleResponse> authCallBack);

    void getInvitations(ApiCallBack<UserInvitationsResponse> invitationCallBack);

    void getInvitation(String invitationId, ApiCallBack<SingeUserInvitationResponse> invitationResponseApiCallBack);

    void selectWinners(String invitationId, ApiCallBack<SingeUserInvitationResponse> invitationsResponseApiCallBack);

    void submitWrapUp(String invitationId, String winnerMsg, String globalMsg, ApiCallBack<SingeUserInvitationResponse> invitationResponseApiCallBack);

    void createOffer(String title, int acceptLimit, long rsvpBy, String emailTo, boolean random, String body, ApiCallBack<SingeUserInvitationResponse> invitationResponseApiCallBack);


}
