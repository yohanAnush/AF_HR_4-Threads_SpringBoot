package com.sliit.se.HR_Spring.repository;

import com.sliit.se.HR_Spring.model.Attendance;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface AttendanceRepository extends MongoRepository<Attendance, String> {

    // all attendance entries of a given employee.
    List<Attendance> getAllByEid(String eid);

    // attendance entry of a given employee on a given date.
    Attendance getAttendancesByEidAndDate(String eid, Date date);

    // attendance entries of a given employee within a given time period.
    List<Attendance> getAttendancesByDateBetweenAndEid(Date start, Date end, String eid);

    // attendance entries of all employees who showed up for work on a given date.
    List<Attendance> getAttendancesByDate(Date date);
}
