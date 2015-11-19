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
import com.pandora.rsvp.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.OnClick;

public class CreateInvitationActivity extends BaseActivity {
    
    @BindString(R.string.create_invitation_activity_title)
    String mTitle;

    @BindString(R.string.create_invitation_activity_invitation_description_blank)
    String mBlankDesc;

    @Bind(R.id.create_invitation_activity_invitation_description_edit_text)
    EditText mDescriptionEdit;

    @Bind(R.id.create_invitation_activity_num_invites_edit_text)
    EditText mNumInvitesEdit;

    @BindString(R.string.create_invitation_activity_num_invitations_blank)
    String mBlankNumInvites;

    @Bind(R.id.create_invitation_activity_email_edit_text)
    EditText mEmailListEdit;

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

        Long datetime = System.currentTimeMillis(); // TODO
        String selectionMethod = mSpinnerChoice.getSelectedItem().toString();
        
        DialogFragment dialogPreviewEmail = PreviewInvitationDialog.newInstance(
                mDescriptionEdit.getText().toString(),                  // Invitation Description
                Integer.parseInt(mNumInvitesEdit.getText().toString()),   // Number of Invitations
                datetime, mEmailListEdit.getText().toString(), selectionMethod);
        dialogPreviewEmail.show(getSupportFragmentManager(), dialogPreviewEmail.getClass().getSimpleName());
    }

    private boolean isBlank(EditText editText) {
        return (editText != null && editText.getText().toString().trim().length() == 0);
    }
}
