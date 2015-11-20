package com.pandora.rsvp.ui;

import com.pandora.rsvp.R;
import com.pandora.rsvp.app.dagger.RSVPComponent;
import com.pandora.rsvp.persistence.IUserDataManager;
import com.pandora.rsvp.service.ApiCallBack;
import com.pandora.rsvp.service.IRSVPApi;
import com.pandora.rsvp.service.contract.SimpleResponse;
import com.pandora.rsvp.ui.base.BaseActivity;
import com.pandora.rsvp.utils.ValidationUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import javax.inject.Inject;

import butterknife.Bind;

public class LoginActivity extends BaseActivity implements ApiCallBack<SimpleResponse> {

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
    @Inject
    IUserDataManager mIUserDataManager;

    @Override
    protected int getActivityLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toggleButton();
        email.addTextChangedListener(validationWatcher);
        password.addTextChangedListener(validationWatcher);
        email.setText(mIUserDataManager.getUserName());
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
        submit.setAlpha(submit.isEnabled() ? 1f : 0.5f);
    }

    @Override
    protected boolean usesToolbar() {
        return false;
    }

    @Override
    public void onSuccess(SimpleResponse successResponse) {
        toggleProgress(false);
        if (successResponse != null && successResponse.success) {
            mIUserDataManager.setUserName(email.getText().toString());
            startActivity(new Intent(LoginActivity.this, InvitationListActivity.class));
        } else {
            onFailure(null);
        }
    }

    @Override
    public void onFailure(Throwable error) {
        toggleProgress(false);
        snackMessage(R.string.auth_failure);

    }

    public void toggleProgress(boolean loading) {
        submit.setText(loading ? "" : getResources().getString(R.string.submit));
        progressBar.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
        submit.setEnabled(!loading);
        submit.setAlpha(submit.isEnabled() ? 1f : 0.5f);
        email.setEnabled(!loading);
        email.setAlpha(email.isEnabled() ? 1f : 0.5f);
        password.setEnabled(!loading);
        password.setAlpha(password.isEnabled() ? 1f : 0.5f);
    }
}
