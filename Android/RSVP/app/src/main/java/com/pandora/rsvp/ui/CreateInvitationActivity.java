package com.pandora.rsvp.ui;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pandora.rsvp.R;
import com.pandora.rsvp.service.ApiCallBack;
import com.pandora.rsvp.service.contract.SingeUserInvitationResponse;
import com.pandora.rsvp.ui.base.BaseActivity;
import com.pandora.rsvp.utils.ValidationUtils;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.OnClick;

public class CreateInvitationActivity extends BaseActivity {
    
    @BindString(R.string.create_invitation_activity_title)
    String mTitle;

    @BindString(R.string.create_invitation_activity_invitation_name_blank)
    String mBlankName;

    @BindString(R.string.create_invitation_activity_invitation_description_blank)
    String mBlankDesc;

    @Bind(R.id.create_invitation_activity_invitation_name_edit_text)
    EditText mNameEdit;
    
    @Bind(R.id.create_invitation_activity_invitation_description_edit_text)
    EditText mDescriptionEdit;

    @Bind(R.id.create_invitation_activity_num_invites_edit_text)
    EditText mNumInvitesEdit;

    @BindString(R.string.create_invitation_activity_num_invitations_blank)
    String mBlankNumInvites;

    @Bind(R.id.create_invitation_activity_email_edit_text)
    EditText mEmailListEdit;

    @BindString(R.string.create_invitation_activity_email_blank)
    String mBlankEmailAddress;

    @BindString(R.string.create_invitation_activity_email_invalid_format)
    String mInvalidEmailAddress;

    @Bind(R.id.create_invitation_activity_spinner_choice)
    Spinner mSpinnerChoice;

    @Bind(R.id.create_invitation_activity_preview_email_button)
    Button mPreviewButton;

    @Override
    protected int getActivityLayoutRes() {
        return R.layout.activity_create_invitation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(mTitle);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.choice_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerChoice.setAdapter(adapter);
    }

    @OnClick(R.id.create_invitation_activity_preview_email_button)
    public void submit(View view) {
        
        if (isBlank(mNameEdit)) { // Validate that the invitation name exists
            Toast.makeText(this, mBlankName, Toast.LENGTH_LONG).show();
            mNameEdit.requestFocus();
            return;
        }
        
        if (isBlank(mDescriptionEdit)) { // Validate that invitation description was entered
            Toast.makeText(this, mBlankDesc, Toast.LENGTH_LONG).show();
            mDescriptionEdit.requestFocus();
            return;
        }

        if (isBlank(mNumInvitesEdit)) { // Validate that number of invitations was entered
            Toast.makeText(this, mBlankNumInvites, Toast.LENGTH_LONG).show();
            mNumInvitesEdit.requestFocus();
            return;
        }
        
        String emailTo;
        if (isBlank(mEmailListEdit)) { // Validate emailTo address
            Toast.makeText(this, mBlankEmailAddress, Toast.LENGTH_LONG).show();
            mEmailListEdit.requestFocus();
            return;
        } else {
            emailTo = mEmailListEdit.getText().toString().trim();
            if (!ValidationUtils.isValidEmail(emailTo)) {
                mEmailListEdit.requestFocus();
                Toast.makeText(this, mInvalidEmailAddress, Toast.LENGTH_LONG).show();
                return;
            }
        }

        Long datetime = System.currentTimeMillis(); // TODO
        String selectionMethod = mSpinnerChoice.getSelectedItem().toString();
        
        DialogFragment dialogPreviewEmail = PreviewInvitationDialog.newInstance(
                mDescriptionEdit.getText().toString(),                  // Invitation Description
                Integer.parseInt(mNumInvitesEdit.getText().toString()),   // Number of Invitations
                datetime, emailTo, mNameEdit.getText().toString(), selectionMethod);
        dialogPreviewEmail.show(getSupportFragmentManager(), dialogPreviewEmail.getClass().getSimpleName());
    }

    private boolean isBlank(EditText editText) {
        return (editText != null && editText.getText().toString().trim().length() == 0);
    }

    //Todo: DONT DELETE
//    api.createOffer("Random Title", Integer.valueOf(mNumInvitesEdit.getText().toString()),
//            Calendar.getInstance().getTimeInMillis(), "dist-rsvp-test@pandora.com", "random",
//            mDescriptionEdit.getText().toString(), new ApiCallBack<SingeUserInvitationResponse>() {
//        @Override
//        public void onSuccess(SingeUserInvitationResponse successResponse) {
//            finish();
//        }
//
//        @Override
//        public void onFailure(Throwable error) {
//            snackMessage("FAILURE!");
//        }
//    });
}
