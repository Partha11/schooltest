package com.example.schoolteacher.Model;

public class NotificationModel {

    private String classId;
    private String from;
    private String type;

    public NotificationModel() {

        //Required for firebase
    }

    public NotificationModel(String classId, String from, String type) {

        this.classId = classId;
        this.from = from;
        this.type = type;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
