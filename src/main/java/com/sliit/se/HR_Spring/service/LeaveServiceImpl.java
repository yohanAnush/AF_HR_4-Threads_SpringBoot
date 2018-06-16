package com.sliit.se.HR_Spring.service;

import com.sliit.se.HR_Spring.model.Leave;
import com.sliit.se.HR_Spring.repository.AttendanceRepository;
import com.sliit.se.HR_Spring.repository.LeaveRepository;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("leaveService")
public class LeaveServiceImpl implements LeaveService {


    @Autowired
    private LeaveRepository leaveRepo;
    @Autowired
    private AttendanceServiceImpl attendanceService;


    @Override
    public List<Leave> getAllLeavesByEmployee(String eid) {
        return leaveRepo.findAllByEid(eid);
    }

    @Override
    public List<Leave> getEmployeesOnLeave(Date date) {
        //List<Leave> leaveList =
         return leaveRepo.findAllByStartAfterAndEndBefore(date, date);
        //List<String> eidList = new ArrayList<>();

        //leaveList.forEach(leave -> eidList.add(leave.getEid()));

        //return eidList;
    }

    @Override
    public List<String> getEmployeesOnLeave(Date start, Date end) {
        return null;
    }

    @Override
    public List<Leave> getAllEntries() {
        return leaveRepo.findAll();
    }

    @Override
    public boolean issueLeave(String eid, Date start, Date end) {
        // get the attendance for the employee for the month of starting the leave.
        DateTime startDT = new DateTime(start);
        int attendedDays = attendanceService.getNumberOfDaysAttendedOnMonth(eid, startDT.getYear(), startDT.getMonthOfYear());
        int potentialLeaveDays = getDateDifferent(start, end);

        return (potentialLeaveDays < 10 && potentialLeaveDays > 0);
    }

    @Override
    public void saveLeave(Leave l) {
        leaveRepo.save(l);
    }

    private int getDateDifferent(Date start, Date end) {
        // we need to convert Date to DateTime which is supported by Joda Time library.
        DateTime startDT = new DateTime(start);
        DateTime endDT = new DateTime(end);
        int difference = 0;

        difference = Days.daysBetween(startDT.toLocalDate(), endDT.toLocalDate()).getDays();

        return difference;
    }
}
