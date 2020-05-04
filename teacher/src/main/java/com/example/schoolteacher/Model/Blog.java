package com.example.schoolteacher.Model;

public class Blog {

    public Blog() {

    }

    public String name, desc, uid, image, status, thumb_image, username, postImage;
    public Object timestamp;


    public Blog(String name, String desc, String uid, String image, String status, String thumb_image, String username, String postImage, Object timestamp) {
        this.name = name;
        this.desc = desc;
        this.uid = uid;
        this.image = image;
        this.status = status;
        this.thumb_image = thumb_image;
        this.username = username;
        this.postImage = postImage;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
