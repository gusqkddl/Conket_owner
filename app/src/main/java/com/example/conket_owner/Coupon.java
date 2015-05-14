package com.example.conket_owner;

import android.text.Editable;

/**
 * Created by user on 2015-05-10.
 */
public class Coupon {
    private String mDetail;
    private boolean mUsedorNot;
    private String mExpired;
    private String mCoupon_Id;
    private String mImage_path;
    private String mStarted;
    private String mShop_id;
    private boolean mIs_pull;

    public Coupon() {

    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String detail) {
        this.mDetail = detail;
    }

    public boolean getUsedornot() {
        return mUsedorNot;
    }

    public void setUsedornot(String usedornot) {
        //사용불가
        if(usedornot.equals("used"))
        {
            mUsedorNot = false;
        }
        //사용가능
        else
            mUsedorNot = true;
    }

    public String getExpired() {
        return mExpired;
    }

    public void setExpired(String expired) {
        this.mExpired = expired;
    }

    public String getStarted() {
        return mStarted;
    }

    public void setStarted(String started) {
        this.mStarted = started;
    }

    public String getImage_path() {
        return mImage_path;
    }

    public void setImage_path(String image_path) {
        this.mImage_path = image_path;
    }

    public String getId() {
        return mCoupon_Id;
    }

    public void setId(String id) {
        this.mCoupon_Id = id;
    }

    public String getShopid() {
        return mShop_id;
    }

    public void setShopid(String shopid) {
        this.mShop_id = shopid;
    }

    public boolean getPull() {
        return mIs_pull;
    }

    public void setPull(boolean is_pull) {
        this.mIs_pull = is_pull;
    }
}


