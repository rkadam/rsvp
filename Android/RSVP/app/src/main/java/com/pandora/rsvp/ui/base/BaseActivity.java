package com.pandora.rsvp.ui.base;

import com.pandora.rsvp.R;
import com.pandora.rsvp.app.RSVPApp;
import com.pandora.rsvp.app.dagger.RSVPComponent;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private View activityContainer;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performInjection(RSVPApp.getComponent());
        setContentView(R.layout.activity_base);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activityContainer = getLayoutInflater().inflate(getActivityLayoutRes(), (ViewGroup) findViewById(R.id.baseContainer));
        ButterKnife.bind(this);
    }

    protected void performInjection(RSVPComponent component) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setVisibility(usesToolbar() ? View.VISIBLE : View.GONE);
    }

    protected boolean usesToolbar() {
        return true;
    }

    protected void snackMessage(String message) {
        Snackbar.make(activityContainer, message, Snackbar.LENGTH_LONG).show();
    }

    protected void snackMessage(int message) {
        Snackbar.make(activityContainer, message, Snackbar.LENGTH_LONG).show();
    }


    protected abstract int getActivityLayoutRes();
}
