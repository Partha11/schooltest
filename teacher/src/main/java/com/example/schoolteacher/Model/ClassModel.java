package com.example.schoolteacher.Model;

import java.util.List;

public class ClassModel {

    private String classId;
    private String className;
    private String createdBy;
    private String createdAt;
    private String description;
    private Integer classFollowers;
    private List<Attendance> attendances;
    private List<Note> notes;
    private List<AssignClass> assignments;
    private List<String> invites;

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

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<AssignClass> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<AssignClass> assignments) {
        this.assignments = assignments;
    }

    public List<String> getInvites() {
        return invites;
    }

    public void setInvites(List<String> invites) {
        this.invites = invites;
    }
}
