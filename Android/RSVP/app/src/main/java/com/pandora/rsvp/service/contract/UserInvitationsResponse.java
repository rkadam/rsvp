package com.pandora.rsvp.service.contract;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class UserInvitationsResponse implements Parcelable {
    public boolean success;
    public String message;
    public List<Invitation> data;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(success ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeTypedList(data);
    }

    public UserInvitationsResponse() {
        this.data = new ArrayList<>();
    }

    private UserInvitationsResponse(Parcel in) {
        this.success = in.readByte() != 0;
        this.message = in.readString();
        this.data = new ArrayList<>();
        in.readTypedList(data, Invitation.CREATOR);
    }

    public static final Parcelable.Creator<UserInvitationsResponse> CREATOR = new Parcelable.Creator<UserInvitationsResponse>() {
        public UserInvitationsResponse createFromParcel(Parcel source) {
            return new UserInvitationsResponse(source);
        }

        public UserInvitationsResponse[] newArray(int size) {
            return new UserInvitationsResponse[size];
        }
    };
}
