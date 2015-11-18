package com.pandora.rsvp.app.dagger;

import com.pandora.rsvp.ui.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
@Singleton
@Component(modules = {RSVPModule.class})
public interface RSVPComponent {
    void inject(LoginActivity loginActivity);
}
