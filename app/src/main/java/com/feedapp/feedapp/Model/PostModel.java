package com.feedapp.feedapp.Model;

import android.arch.lifecycle.ViewModel;
import android.os.Parcel;
import android.os.Parcelable;

public class PostModel {

    String userid;
    String postId;
    Object timeStamp;
    String postTitle;
    String postData;

    public PostModel() {

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostData() {
        return postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }


    @Override
    public String toString() {
        return "PostModel{" +
                "userid='" + userid + '\'' +
                ", postId='" + postId + '\'' +
                ", timeStamp=" + timeStamp +
                ", postTitle='" + postTitle + '\'' +
                ", postData='" + postData + '\'' +
                '}';
    }
}
