package com.sliit.se.HR_Spring.service;

import com.sliit.se.HR_Spring.model.Attendance;
import com.sliit.se.HR_Spring.repository.AttendanceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("attendanceService")
public class AttendanceServiceImpl implements AttendanceService {


    @Autowired
    private AttendanceRepository attendanceRepo;

    @Override
    public List<Attendance> getAllEntries() {
        return attendanceRepo.findAll();
    }

    @Override
    public List<Attendance> getAllAttendanceOfEmployee(String eid) {
        return attendanceRepo.getAllByEid(eid);
    }

    @Override
    public List<Attendance> getAllAttendanceOfEmployeeDuring(Date start, Date end, String eid) {
        return attendanceRepo.getAttendancesByDateBetweenAndEid(start, end, eid);
    }

    @Override
    public List<String> getAllEmployeesAttendedOn(Date date) {
        List<Attendance> attendanceList = attendanceRepo.getAttendancesByDate(date);
        List<String> employeeIds = new ArrayList<>();

        // filter the eid of each entry.
        attendanceList.forEach(attendance -> employeeIds.add(attendance.getEid()));

        return employeeIds;
    }


    @Override
    public int getNumberOfDaysAttendedOnMonth(String eid, int year, int month) {
        // to isolate a certain month, we can use getAllAttendanceOfEmployeeDuring method,
        // and provide the start and end as the first day of the relevant month and the first day of the next month.
        Calendar start = Calendar.getInstance();
        start.set(year, month-1 , 1); // convert to Date since mongodb deals with Date not Calendar; month ranges from 0 to 11.

        Calendar end = Calendar.getInstance();
        end.set(year, month-1, 31);  // convert to Date since mongodb deals with Date not Calendar; month ranges from 0 to 11.


        List<Attendance> attendanceList = attendanceRepo.getAttendancesByDateBetweenAndEid(start.getTime(), end.getTime(), eid);

        return attendanceList.size();
    }

    @Override
    public void addAttendanceEntry(Attendance a) {
        attendanceRepo.save(a);
    }
}
