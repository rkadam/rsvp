package com.pandora.rsvp.ui;

import com.pandora.rsvp.R;
import com.pandora.rsvp.app.dagger.RSVPComponent;
import com.pandora.rsvp.service.ApiCallBack;
import com.pandora.rsvp.service.IRSVPApi;
import com.pandora.rsvp.ui.base.BaseActivity;
import com.pandora.rsvp.utils.ValidationUtils;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import javax.inject.Inject;

import butterknife.Bind;

public class LoginActivity extends BaseActivity implements ApiCallBack<Boolean> {

    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.submit)
    Button submit;
    @Bind(R.id.progress)
    ProgressBar progressBar;
    @Bind(R.id.fields_container)
    LinearLayout fieldsContainer;

    @Inject
    IRSVPApi mIRSVPApi;

    @Override
    protected int getActivityLayoutRes() {
        return R.layout.content_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toggleButton();
        email.addTextChangedListener(validationWatcher);
        password.addTextChangedListener(validationWatcher);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        mIRSVPApi.authenticate(email.getText().toString(), password.getText().toString(), this);
        toggleProgress(true);
    }

    @Override
    protected void performInjection(RSVPComponent component) {
        component.inject(this);
    }

    private TextWatcher validationWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            toggleButton();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void toggleButton() {
        String pass = password.getText().toString();
        boolean valid = ValidationUtils.isValidEmail(email.getText()) && !pass.isEmpty();
        submit.setEnabled(valid);
    }

    @Override
    protected boolean usesToolbar() {
        return false;
    }

    @Override
    public void onSuccess(Boolean successResponse) {
        toggleProgress(false);
    }

    @Override
    public void onFailure(Throwable error) {
        toggleProgress(false);
        snackMessage(R.string.auth_failure);

    }

    public void toggleProgress(boolean loading) {
        fieldsContainer.setVisibility(loading ? View.INVISIBLE : View.VISIBLE);
        progressBar.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
    }
}
