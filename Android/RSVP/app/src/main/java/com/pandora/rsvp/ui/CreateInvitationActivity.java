package com.pandora.rsvp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.pandora.rsvp.R;
import com.pandora.rsvp.app.dagger.RSVPComponent;
import com.pandora.rsvp.persistence.IUserDataManager;
import com.pandora.rsvp.service.ApiCallBack;
import com.pandora.rsvp.service.IRSVPApi;
import com.pandora.rsvp.service.contract.SingeUserInvitationResponse;
import com.pandora.rsvp.ui.base.BaseActivity;
import com.pandora.rsvp.utils.ValidationUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

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
    @Bind(R.id.fields_layout)
    RelativeLayout fieldsLayout;
    @Bind(R.id.progress)
    ProgressBar pbLoading;
    @Bind(R.id.create_invitation_activity_datetime_picker)
    Button mDateTimeButton;
    @Bind(R.id.create_invitation_activity_rsvp_date)
    TextView mRSVPDate;

    @Inject
    IUserDataManager mIUserDataManager;
    @Inject
    IRSVPApi mIRSVPApi;

    @Override
    protected int getActivityLayoutRes() {
        return R.layout.activity_create_invitation;
    }

    @Override
    protected void performInjection(RSVPComponent component) {
        component.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(mTitle);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.choice_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerChoice.setAdapter(adapter);
        mEmailListEdit.setText("rsvp-test@pandora.com");
        toggleProgress(false);

        mDateTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                CalendarDatePickerDialog dialog = CalendarDatePickerDialog.newInstance(dateCallBack,
                        now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                dialog.show(getSupportFragmentManager(), "DATE_DIALOG");
            }
        });
    }

    @OnClick(R.id.create_invitation_activity_preview_email_button)
    public void submit(View view) {

        if (isBlank(mNameEdit)) {
            snackMessage(mBlankName);
            mNameEdit.requestFocus();
            return;
        }

        if (isBlank(mDescriptionEdit)) {
            snackMessage(mBlankDesc);
            mDescriptionEdit.requestFocus();
            return;
        }

        if (isBlank(mNumInvitesEdit)) {
            snackMessage(mBlankNumInvites);
            mNumInvitesEdit.requestFocus();
            return;
        }

        final String emailTo;
        if (isBlank(mEmailListEdit)) {
            snackMessage(mBlankEmailAddress);
            mEmailListEdit.requestFocus();
            return;
        } else {
            emailTo = mEmailListEdit.getText().toString().trim();
            if (!ValidationUtils.isValidEmail(emailTo)) {
                mEmailListEdit.requestFocus();
                snackMessage(mInvalidEmailAddress);
                return;
            }
        }

        final Long datetime = System.currentTimeMillis(); // 
        String selectionMethod = mSpinnerChoice.getSelectedItem().toString();
        final boolean random = selectionMethod.equalsIgnoreCase("Random Order");
        PreviewInvitationDialog dialogPreviewEmail = PreviewInvitationDialog.newInstance(
                mDescriptionEdit.getText().toString(),                  // Invitation Description
                Integer.parseInt(mNumInvitesEdit.getText().toString()),   // Number of Invitations
                datetime, emailTo, mIUserDataManager.getUserName(), mNameEdit.getText().toString(), selectionMethod);
        dialogPreviewEmail.setCreateInviteListener(new PreviewInvitationDialog.CreateInviteListener() {
            @Override
            public void create() {
                toggleProgress(true);
                mIRSVPApi.createOffer(mNameEdit.getText().toString(), Integer.valueOf(mNumInvitesEdit.getText().toString()),
                        datetime, emailTo, random, mDescriptionEdit.getText().toString(), new ApiCallBack<SingeUserInvitationResponse>() {
                            @Override
                            public void onSuccess(SingeUserInvitationResponse successResponse) {
                                if (successResponse != null && successResponse.data != null) {
                                    Intent intent = new Intent(CreateInvitationActivity.this, InvitationResponsesActivity.class);
                                    intent.putExtra(InvitationResponsesActivity.INVITATION_KEY, successResponse.data);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    onFailure(null);
                                }
                            }

                            @Override
                            public void onFailure(Throwable error) {
                                toggleProgress(false);
                                snackMessage(R.string.invite_create_failed);
                            }
                        });
            }
        });
        dialogPreviewEmail.show(getSupportFragmentManager(), dialogPreviewEmail.getClass().getSimpleName());
    }

    private void toggleProgress(boolean loading) {
        pbLoading.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
        fieldsLayout.setVisibility(loading ? View.INVISIBLE : View.VISIBLE);
    }

    private boolean isBlank(EditText editText) {
        return (editText != null && editText.getText().toString().trim().length() == 0);
    }

    private Calendar pickedTime;
    private boolean timeSet;
    private static final String DATE_TIME_FORMAT = "MM/dd/yyyy HH:mm Z";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);

    private CalendarDatePickerDialog.OnDateSetListener dateCallBack = new CalendarDatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(CalendarDatePickerDialog calendarDatePickerDialog, int year, int monthOfYear, int dayOfMonth) {
            pickedTime = Calendar.getInstance();
            pickedTime.set(Calendar.YEAR, year);
            pickedTime.set(Calendar.MONTH, monthOfYear);
            pickedTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            RadialTimePickerDialog dialog = RadialTimePickerDialog.newInstance(onTimeSetListener, pickedTime.get(Calendar.HOUR_OF_DAY),
                    pickedTime.get(Calendar.MINUTE), false);
            dialog.show(getSupportFragmentManager(), "TIME_DIALOG");
        }
    };

    private RadialTimePickerDialog.OnTimeSetListener onTimeSetListener = new RadialTimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(RadialTimePickerDialog radialTimePickerDialog, int hourOfDay, int minute) {
            timeSet = true;
            pickedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            pickedTime.set(Calendar.MINUTE, minute);
            mRSVPDate.setText(simpleDateFormat.format(pickedTime.getTime()));
        }
    };

}
