package com.example.conket_owner;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2015-05-11.
 */
public class User implements Parcelable {
    private String mId;
    private String mName;
    private String mPhone;


    public User() {
    }

    public User(Parcel in) {
        readFromParcel(in);
    }

    public User(String id, String name, String phone) {
        this.mId = id;
        this.mName = name;
        this.mPhone = phone;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mPhone);
    }

    private void readFromParcel(Parcel in)
    {
        mId = in.readString();
        mName = in.readString();
        mPhone = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
