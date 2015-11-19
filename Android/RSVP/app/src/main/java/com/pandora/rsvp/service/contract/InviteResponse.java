package com.pandora.rsvp.service.contract;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class InviteResponse implements Parcelable {
    public String uid;
    public String inviation_id;
    public String name;
    public int years;
    public String department;
    public long reponded;
    public String response_body;
    public boolean selected;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.inviation_id);
        dest.writeString(this.name);
        dest.writeInt(this.years);
        dest.writeString(this.department);
        dest.writeLong(this.reponded);
        dest.writeString(this.response_body);
        dest.writeByte(selected ? (byte) 1 : (byte) 0);
    }

    public InviteResponse() {
    }

    private InviteResponse(Parcel in) {
        this.uid = in.readString();
        this.inviation_id = in.readString();
        this.name = in.readString();
        this.years = in.readInt();
        this.department = in.readString();
        this.reponded = in.readLong();
        this.response_body = in.readString();
        this.selected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<InviteResponse> CREATOR = new Parcelable.Creator<InviteResponse>() {
        public InviteResponse createFromParcel(Parcel source) {
            return new InviteResponse(source);
        }

        public InviteResponse[] newArray(int size) {
            return new InviteResponse[size];
        }
    };
}
