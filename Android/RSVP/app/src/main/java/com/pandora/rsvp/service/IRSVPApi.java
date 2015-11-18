package com.pandora.rsvp.service;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public interface IRSVPApi {

    void authenticate(String email, String password, ApiCallBack<Boolean> authCallback);

}
