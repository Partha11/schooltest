package com.example.schoolteacher.Model;

public class Assignment {

    private String assignmentId;
    private String assignmentTitle;
    private String assignmentDescription;
    private Integer assignmentPoints = 0;
    private String dueDate = "No Due Date";

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public String getAssignmentDescription() {
        return assignmentDescription;
    }

    public void setAssignmentDescription(String assignmentDescription) {
        this.assignmentDescription = assignmentDescription;
    }

    public Integer getAssignmentPoints() {
        return assignmentPoints;
    }

    public void setAssignmentPoints(Integer assignmentPoints) {
        this.assignmentPoints = assignmentPoints;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
