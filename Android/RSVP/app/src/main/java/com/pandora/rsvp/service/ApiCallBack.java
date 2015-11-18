package com.pandora.rsvp.service;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public interface ApiCallBack<T> {

    void onSuccess(T successResponse);

    void onFailure(Throwable error);

}
