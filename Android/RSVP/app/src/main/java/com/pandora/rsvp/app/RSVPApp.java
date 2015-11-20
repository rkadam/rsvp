package com.pandora.rsvp.app;

import com.pandora.rsvp.R;
import com.pandora.rsvp.app.dagger.DaggerRSVPComponent;
import com.pandora.rsvp.app.dagger.RSVPComponent;
import com.pandora.rsvp.app.dagger.RSVPModule;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class RSVPApp extends Application {

    private static RSVPApp sApp;

    private RSVPComponent mRSVPComponent;

    public RSVPApp() {
        sApp = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        buildComponent();
    }

    public static RSVPComponent getComponent() {
        return sApp.mRSVPComponent;
    }

    private void buildComponent() {
        mRSVPComponent = DaggerRSVPComponent.builder()
                .rSVPModule(new RSVPModule(this))
                .build();
    }


}
