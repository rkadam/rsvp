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
import android.widget.Toast;

/*
        DialogFragment dialog = WrapUpMessageDialog.newInstance(bundle);
        dialog.show(getSupportFragmentManager(), dialog.getClass().getSimpleName());
 */

public class WrapUpMessageDialog extends DialogFragment {
    public static final String KEY_WINNER_MSG = "key_winner_message";
    public static final String KEY_EVERYONE_MSG = "key_everyone_message";

    private EditText mWinnerMsgEdit;
    private EditText mAllResponsesEdit;
    private Button mSendButton;    
    
    public static WrapUpMessageDialog newInstance(Bundle bundle) {
        WrapUpMessageDialog emailPreviewDialog = new WrapUpMessageDialog();
        emailPreviewDialog.setArguments(bundle);
        return emailPreviewDialog;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mWinnerMsgEdit = (EditText)view.findViewById(R.id.dialog_email_preview_winner_edit_text);
        mAllResponsesEdit = (EditText)view.findViewById(R.id.dialog_email_preview_all_responses_edit_text);
        mSendButton = (Button)view.findViewById(R.id.dialog_wrap_up_send_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBlank(mWinnerMsgEdit)) {
                    mWinnerMsgEdit.requestFocus();
                    Toast.makeText(getActivity(), getResources().getString(R.string.dialog_wrap_up_messages_empty_winner_message),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (isBlank(mAllResponsesEdit)) {
                    mAllResponsesEdit.requestFocus();
                    Toast.makeText(getActivity(), getResources().getString(R.string.dialog_wrap_up_messages_empty_all_message),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString(KEY_WINNER_MSG, mWinnerMsgEdit.getText().toString());
                bundle.putString(KEY_EVERYONE_MSG, mAllResponsesEdit.getText().toString());

                // TODO - launch new activity
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_wrap_up_messages, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private boolean isBlank(EditText editText) {
        return (editText != null && editText.getText().toString().trim().length() == 0);
    }

}