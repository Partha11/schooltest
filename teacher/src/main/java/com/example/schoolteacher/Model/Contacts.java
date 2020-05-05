package com.example.schoolteacher.Model;

public class Contacts {

    private String classId;

    public Contacts() {
        //Empty
    }

    public Contacts(String name, String status, String image) {

        this.name = name;
        this.status = status;
        this.image = image;
    }

    public String name, status, image;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
