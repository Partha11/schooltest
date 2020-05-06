package com.example.schoolteacher.Model;

import java.util.HashMap;

public class Grade {

    private String gradeId;
    private String date;
    private HashMap<String, String> grades;

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
}
