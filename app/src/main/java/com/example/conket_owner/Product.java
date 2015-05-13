package com.example.conket_owner;

/**
 * Created by user on 2015-05-10.
 */
public class Product {
    private String mName;
    private String mOrigin;
    private String mPrice;
    private String mImage_path;
    private String mShop_Id;

    public Product() {

    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getNumber() {
        return mOrigin;
    }

    public void setNumber(String origin) {
        this.mOrigin = origin;
    }

    public String getComment() {
        return mPrice;
    }

    public void setComment(String price) {
        this.mPrice = price;
    }

    public String getImage_path() {
        return mImage_path;
    }

    public void setImage_path(String image_path) {
        this.mImage_path = image_path;
    }

    public String getId() {
        return mShop_Id;
    }

    public void setId(String id) {
        this.mShop_Id = id;
    }
}
