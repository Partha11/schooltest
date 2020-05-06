package com.example.schoolteacher.Model;

import java.util.HashMap;

public class Attendance {

    private String attendanceId;
    private String attendanceDate;
    private HashMap<String, Boolean> attendance;

    public Attendance(String attendanceId, String attendanceDate) {

        this.attendanceId = attendanceId;
        this.attendanceDate = attendanceDate;
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

    public HashMap<String, Boolean> getAttendance() {
        return attendance;
    }

    public void setAttendance(HashMap<String, Boolean> attendance) {
        this.attendance = attendance;
    }
}
