package com.pandora.rsvp.app.dagger;

import com.pandora.rsvp.service.IRSVPApi;
import com.pandora.rsvp.service.impl.RSVPApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
@Module
public class RSVPModule {

    @Singleton
    @Provides
    IRSVPApi provideRSVPApi() {
        return new RSVPApi();
    }
}
