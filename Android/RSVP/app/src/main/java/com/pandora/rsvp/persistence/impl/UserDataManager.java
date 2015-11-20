package com.pandora.rsvp.persistence.impl;

import com.pandora.rsvp.app.RSVPApp;
import com.pandora.rsvp.persistence.IUserDataManager;

import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class UserDataManager implements IUserDataManager {

    private static final String KEY_USERNAME = "UserNameKey";
    @Inject
    SharedPreferences mSharedPreferences;

    public UserDataManager() {
        RSVPApp.getComponent().inject(this);
    }

    @Override
    public void setUserName(String userName) {
        mSharedPreferences.edit().putString(KEY_USERNAME, userName).apply();
    }

    @Override
    public String getUserName() {
        return mSharedPreferences.getString(KEY_USERNAME, "");
    }
}
