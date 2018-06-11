package com.sliit.se.HR_Spring.service;

import com.sliit.se.HR_Spring.model.Attendance;

import java.util.Date;
import java.util.List;

public interface AttendanceService {

    public List<Attendance> getAllEntries();

    public List<Attendance> getAllAttendanceOfEmployee(String eid);

    public List<Attendance> getAllAttendanceOfEmployeeDuring(Date start, Date end, String eid);

    public List<String> getAllEmployeesAttendedOn(Date date);

    public int getNumberOfDaysAttendedOnMonth(String eid, int year, int month);

    public void addAttendanceEntry(Attendance a);
}
