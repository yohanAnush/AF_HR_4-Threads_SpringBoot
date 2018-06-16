package com.sliit.se.HR_Spring.service;

import com.sliit.se.HR_Spring.model.Leave;

import java.util.Date;
import java.util.List;

public interface LeaveService {

    // leaves history of an employee.
    public List<Leave> getAllLeavesByEmployee(String eid);

    // get the eids of all the employees on leave for a given day(date).
    public List<Leave> getEmployeesOnLeave(Date date);

    // get the eids of all employees on leave within a given time period.
    // the leave may end after the time period we are considering, but as long as,
    // the leave start within the time period we consider, it's counted.
    public List<String> getEmployeesOnLeave(Date start, Date end);

    public List<Leave> getAllEntries();

    public boolean issueLeave(String eid, Date start, Date end);

    public void saveLeave(Leave l);
}
