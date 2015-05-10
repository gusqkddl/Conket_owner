package com.example.conket_owner;

import java.util.Date;

/**
 * Created by user on 2015-05-10.
 */
public class Review {
    private String mUser_id;
    private String mReview;
    private String mDate;
    private String mReview_id;
    private String mImage_path;

    public Review() {

    }

    public Review(String user_id, String review, String date, String review_id, String image_path) {
        this.mUser_id = user_id;
        this.mReview = review;
        this.mDate = date;
        this.mImage_path = image_path;
        this.mReview_id = mReview_id;
    }

    public String getUser_Id() {
        return mUser_id;
    }

    public void setUser_Id(String user_id) {
        this.mUser_id = user_id;
    }

    public String getReview() {
        return mReview;
    }

    public void setReview(String review) {
        this.mReview = review;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getImage_path() {
        return mImage_path;
    }

    public void setImage_path(String image_path) {
        this.mImage_path = image_path;
    }

    public String getReview_Id() {
        return mReview_id;
    }

    public void setId(String review_id) {
        this.mReview_id = review_id;
    }
}
