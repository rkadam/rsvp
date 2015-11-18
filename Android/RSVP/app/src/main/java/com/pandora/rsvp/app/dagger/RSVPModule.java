package com.pandora.rsvp.app.dagger;

import com.pandora.rsvp.app.RSVPApp;
import com.pandora.rsvp.service.IRSVPApi;
import com.pandora.rsvp.service.impl.RSVPApi;
import com.squareup.picasso.Picasso;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
@Module
public class RSVPModule {
    private RSVPApp app;

    public RSVPModule(RSVPApp app) {
        this.app = app;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return app;
    }

    @Singleton
    @Provides
    IRSVPApi provideRSVPApi() {
        return new RSVPApi();
    }

    @Singleton
    @Provides
    Picasso providePicasso(Context context) {
        return Picasso.with(context);
    }
}
