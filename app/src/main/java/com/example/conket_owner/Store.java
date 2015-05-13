package com.example.conket_owner;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.apache.http.ParseException;

/**
 * Created by user on 2015-05-10.
 */
public class Store {
    private String mName;//상호명
    private String mNumber;//사업자 번호
    private String mPhone; //전화번호
    private String mComment;//상점 정보
    private Bitmap mImage; //이미지
    private String mImage_path;
    private String mId;
    private String mBeacon;//비콘ID
    private String mUser_id;

    public Store() {

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

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        this.mNumber = number;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        this.mComment = comment;
    }

    public String getImage_path() {
        return mImage_path;
    }

    public void setImage_path(String image_path) {
        this.mImage_path = image_path;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getBeacon() {
        return mBeacon;
    }

    public void setBeacon(String beacon) {
        this.mBeacon = beacon;
    }

    public String getUser_id() {
        return mUser_id;
    }

    public void setUser_id(String user_id) {
        this.mUser_id = user_id;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap image) {
        this.mImage = image;
    }
}
