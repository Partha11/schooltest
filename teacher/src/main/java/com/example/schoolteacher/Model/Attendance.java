package com.example.schoolteacher.Model;

public class Attendance {

    private Integer totalClasses;
    private String attendanceId;
    private String attendanceDate;

    public Attendance(Integer totalClasses, String attendanceId, String attendanceDate) {

        this.totalClasses = totalClasses;
        this.attendanceId = attendanceId;
        this.attendanceDate = attendanceDate;
    }

    public Integer getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(Integer totalClasses) {
        this.totalClasses = totalClasses;
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }
}
