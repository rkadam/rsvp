package com.pandora.rsvp.ui;

import com.pandora.rsvp.R;
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

import butterknife.Bind;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.submit)
    Button submit;
    

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
}
