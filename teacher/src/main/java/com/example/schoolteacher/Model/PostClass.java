package com.example.schoolteacher.Model;


public class PostClass {

    public String postId;
    public String postTitle;
    public String post;
    public String postDesc;


    public PostClass() {
    }

    public PostClass(String postId, String postTitle, String post) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.post = post;
    }

    public PostClass(String postId, String post) {
        this.postId = postId;
        this.post = post;

    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }




}

