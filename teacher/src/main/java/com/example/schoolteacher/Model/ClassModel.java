package com.example.schoolteacher.Model;

public class ClassModel {

    private String classId;
    private String className;
    private String createdBy;
    private String createdAt;
    private String description;
    private Integer classFollowers;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getClassFollowers() {
        return classFollowers;
    }

    public void setClassFollowers(Integer classFollowers) {
        this.classFollowers = classFollowers;
    }
}
