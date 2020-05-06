package com.example.schoolteacher.Model;

import java.util.HashMap;

public class Grade {

    private String gradeId;
    private String date;
    private String className;
    private String parentName;
    private String parentStatus;
    private String grade;
    private HashMap<String, String> grades;

    public Grade() {
        //Empty
    }

    public Grade(String gradeId, String date) {

        this.gradeId = gradeId;
        this.date = date;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public HashMap<String, String> getGrades() {
        return grades;
    }

    public void setGrades(HashMap<String, String> grades) {
        this.grades = grades;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentStatus() {
        return parentStatus;
    }

    public void setParentStatus(String parentStatus) {
        this.parentStatus = parentStatus;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
