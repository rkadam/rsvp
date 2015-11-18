package com.pandora.rsvp.base;

import com.pandora.rsvp.R;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getLayoutInflater().inflate(getActivityLayoutRes(), (ViewGroup) findViewById(R.id.baseContainer));

    }

    protected abstract int getActivityLayoutRes();

}
