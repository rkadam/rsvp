package com.pandora.rsvp.ui;

import com.pandora.rsvp.R;
import com.pandora.rsvp.ui.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    @Override
    protected int getActivityLayoutRes() {
        return R.layout.content_login;
    }

    @Override
    protected boolean usesToolbar() {
        return false;
    }
}
