package com.pandora.rsvp.ui.base;

import com.pandora.rsvp.R;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getLayoutInflater().inflate(getActivityLayoutRes(), (ViewGroup) findViewById(R.id.baseContainer));

    }


    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setVisibility(usesToolbar() ? View.VISIBLE : View.GONE);
    }

    protected boolean usesToolbar() {
        return true;
    }

    protected abstract int getActivityLayoutRes();

}
