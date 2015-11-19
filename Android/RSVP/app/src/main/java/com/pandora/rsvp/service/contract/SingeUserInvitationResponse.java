package com.pandora.rsvp.service.contract;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class SingeUserInvitationResponse implements Parcelable {
    public boolean success;
    public String message;
    public Invitation data;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(success ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeParcelable(this.data, 0);
    }

    public SingeUserInvitationResponse() {
    }

    private SingeUserInvitationResponse(Parcel in) {
        this.success = in.readByte() != 0;
        this.message = in.readString();
        this.data = in.readParcelable(Invitation.class.getClassLoader());
    }

    public static final Parcelable.Creator<SingeUserInvitationResponse> CREATOR = new Parcelable.Creator<SingeUserInvitationResponse>() {
        public SingeUserInvitationResponse createFromParcel(Parcel source) {
            return new SingeUserInvitationResponse(source);
        }

        public SingeUserInvitationResponse[] newArray(int size) {
            return new SingeUserInvitationResponse[size];
        }
    };
}
