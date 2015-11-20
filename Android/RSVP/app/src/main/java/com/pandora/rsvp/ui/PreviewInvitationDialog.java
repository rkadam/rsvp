package com.pandora.rsvp.ui;

import com.pandora.rsvp.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PreviewInvitationDialog extends DialogFragment {
    public static final String KEY_INVITATION_DESCRIPTION = "key_invitation_description";
    public static final String KEY_NUM_INVITES = "key_num_invites";
    public static final String KEY_RSVP_DATE = "key_rsvp_date";
    public static final String KEY_RSVP_TIME = "key_rsvp_time";
    public static final String KEY_EMAIL_TO = "key_email_to";
    public static final String KEY_EMAIL_FROM = "key_email_from";
    public static final String KEY_EMAIL_SUBJECT = "key_email_subject";

    public static final String KEY_METHOD_CHOICE = "key_method_choice";
    
    private Button mCancelButton;
    private Button mSendButton;
    
    public static PreviewInvitationDialog newInstance(String description, int numInvitations,
                                                      long datetime, String emailTo, String emailSubject, String selectionMethod) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_INVITATION_DESCRIPTION, description);
        bundle.putInt(KEY_NUM_INVITES, numInvitations);
        bundle.putLong(KEY_RSVP_DATE, datetime);
        bundle.putString(KEY_EMAIL_TO, emailTo);
        bundle.putString(KEY_EMAIL_SUBJECT, emailSubject);
        bundle.putString(KEY_METHOD_CHOICE, selectionMethod);
        // TODO - need to pass in from and subject
                
        PreviewInvitationDialog emailPreviewDialog = new PreviewInvitationDialog();
        emailPreviewDialog.setArguments(bundle);
        return emailPreviewDialog;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        TextView to = (TextView)view.findViewById(R.id.dialog_invitation_preview_to_text);
        to.setText(getArguments().getString(KEY_EMAIL_TO));
        
        TextView from = (TextView)view.findViewById(R.id.dialog_invitation_preview_from_text);
//        from.setText(getArguments().getString(KEY_EMAIL_FROM)); // TODO - get from userPrefs
        
        TextView subject = (TextView)view.findViewById(R.id.dialog_invitation_preview_subject_text);
        subject.setText(getArguments().getString(KEY_EMAIL_SUBJECT));

        TextView emailBody = (TextView)view.findViewById(R.id.dialog_invitation_preview_message_body);
        emailBody.setText(getArguments().getString(KEY_INVITATION_DESCRIPTION));

        mCancelButton = (Button)view.findViewById(R.id.dialog_invitation_preview_cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mSendButton = (Button)view.findViewById(R.id.dialog_invitation_preview_send_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO - API call here
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_invitation_preview, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}