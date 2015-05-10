package com.example.conket_owner;

/**
 * Created by user on 2015-05-10.
 */
public class Coupon {
    private String mDetail;
    private boolean mUsedorNot;
    private String mExpired;
    private String mCoupon_Id;
    private String mImage_path;

    public Coupon() {

    }

    public Coupon(String detail, boolean usedornot, String expired, String image_path, String coupon_id) {
        this.mDetail = detail;
        this.mUsedorNot = usedornot;
        this.mExpired = expired;
        this.mImage_path = image_path;
        this.mCoupon_Id = coupon_id;
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
}

