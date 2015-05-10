package com.example.conket_owner;

/**
 * Created by user on 2015-05-10.
 */
public class Store {
    private String mName;
    private String mNumber;
    private String mComment;
    private String mImage_path;
    private String mId;

    public Store() {

    }

    public Store(String name, String number, String comment, String image_path, String id) {
        this.mName = name;
        this.mNumber = number;
        this.mComment = comment;
        this.mImage_path = image_path;
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
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
}
