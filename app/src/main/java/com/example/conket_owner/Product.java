package com.example.conket_owner;

/**
 * Created by user on 2015-05-10.
 */
public class Product {
    private String mId;
    private String mName;
    private String mOrigin;
    private String mPrice;
    private String mDate;
    private String mCount;
    private String mComment;
    private String mImage_path;
    private String mShop_Id;
    private String mHowcount;
    private boolean isNew;
    private boolean isTimely;

    public Product() {

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

    public String getOrigin() {
        return mOrigin;
    }

    public void setOrigin(String origin) {
        this.mOrigin = origin;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        this.mPrice = price;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getCount() {
        return mCount;
    }

    public void setCount(String count) {
        this.mCount = count;
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

    public String getShopid() {
        return mShop_Id;
    }

    public void setShopid(String shop_id) {
        this.mShop_Id = shop_id;
    }

    public String getHowcount() {
        return mHowcount;
    }

    public void setHowcount(String howcount) {
        this.mHowcount = howcount;
    }

    public boolean getNew() {
        return this.isNew;
    }

    public void setNew(boolean is_new) {
        isNew = is_new;
    }

    public boolean getTimely() {
        return this.isTimely;
    }

    public void setTimely(boolean is_timely) {
        isTimely = is_timely;
    }
}
