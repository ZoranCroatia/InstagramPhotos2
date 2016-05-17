package com.android.thestateofperfection84zb;

/**
 * I created this class because I will need PhotoResource object for the class PhotoAdapter
 */
public class PhotoResource {

    long createdTime;
    String images;
    String caption;
    String user;
    String userURL;
    long likeCount;

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String owner) {
        this.user = owner;
    }

    public String getUserURL() {
        return userURL;
    }

    public void setUserURL(String userURL) {
        this.userURL = userURL;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

}
