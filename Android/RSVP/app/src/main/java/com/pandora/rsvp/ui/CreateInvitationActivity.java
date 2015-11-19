package com.pandora.rsvp.ui;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
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
    public static final String KEY_INVITATION_DESCRIPTION = "key_invitation_description";
    public static final String KEY_NUM_INVITES = "key_num_invites";
    public static final String KEY_RSVP_DATE = "key_rsvp_date";
    public static final String KEY_RSVP_TIME = "key_rsvp_time";
    public static final String KEY_EMAIL = "key_email";
    public static final String KEY_METHOD_CHOICE = "key_method_choice";

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
        Log.e("MSW", "Button !!!");
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

        Bundle bundle = new Bundle();
        bundle.putString(KEY_INVITATION_DESCRIPTION, mDescriptionEdit.getText().toString());
        bundle.putInt(KEY_NUM_INVITES, Integer.parseInt(mNumInvitesEdit.getText().toString()));

        DialogFragment dialog = WrapUpMessageDialog.newInstance(bundle);
        dialog.show(getSupportFragmentManager(), dialog.getClass().getSimpleName());
    }

    private boolean isBlank(EditText editText) {
        return (editText != null && editText.getText().toString().trim().length() == 0);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_create_invitation, menu);
//        return true;
//    }
//    
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
    
    
//TODO: Don't delete for now!!!
//    api.createOffer("RandomOffer NumSomething", Integer.parseInt(mNumInvitesEdit.getText().toString()), date,
//            "dist-rsvp-test@pandora.com", "random",
//            mDescriptionEdit.getText().toString(), this);
}
