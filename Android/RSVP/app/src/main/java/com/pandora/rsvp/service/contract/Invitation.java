package com.pandora.rsvp.service.contract;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class Invitation implements Parcelable {
    public String id;
    public String uid;
    public String title;
    public int response_accept_limit;
    public long create_time;
    public long rsvp_by_time;
    public String email_to;
    public String method;
    public String invitation_body;
    public String accepted_body;
    public String rejected_body;
    public boolean active;
    public List<InviteResponse> responses;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.uid);
        dest.writeString(this.title);
        dest.writeInt(this.response_accept_limit);
        dest.writeLong(this.create_time);
        dest.writeLong(this.rsvp_by_time);
        dest.writeString(this.email_to);
        dest.writeString(this.method);
        dest.writeString(this.invitation_body);
        dest.writeString(this.accepted_body);
        dest.writeString(this.rejected_body);
        dest.writeByte(active ? (byte) 1 : (byte) 0);
        dest.writeTypedList(responses);
    }

    public Invitation() {
        this.responses = new ArrayList<>();
    }

    private Invitation(Parcel in) {
        this.id = in.readString();
        this.uid = in.readString();
        this.title = in.readString();
        this.response_accept_limit = in.readInt();
        this.create_time = in.readLong();
        this.rsvp_by_time = in.readLong();
        this.email_to = in.readString();
        this.method = in.readString();
        this.invitation_body = in.readString();
        this.accepted_body = in.readString();
        this.rejected_body = in.readString();
        this.active = in.readByte() != 0;
        responses = new ArrayList<>();
        in.readTypedList(responses, InviteResponse.CREATOR);
    }

    public static final Parcelable.Creator<Invitation> CREATOR = new Parcelable.Creator<Invitation>() {
        public Invitation createFromParcel(Parcel source) {
            return new Invitation(source);
        }

        public Invitation[] newArray(int size) {
            return new Invitation[size];
        }
    };
}
