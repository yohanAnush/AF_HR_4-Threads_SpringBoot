package com.sliit.se.HR_Spring.service;

import com.sliit.se.HR_Spring.model.Leave;
import com.sliit.se.HR_Spring.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("leaveService")
public class LeaveServiceImpl implements LeaveService {


    @Autowired
    private LeaveRepository leaveRepo;

    @Override
    public List<Leave> getAllLeavesByEmployee(String eid) {
        return leaveRepo.findAllByEid(eid);
    }

    @Override
    public List<String> getEmployeesOnLeave(Date date) {
        List<Leave> leaveList = leaveRepo.findAllByStartAfterAndEndBefore(date, date);
        List<String> eidList = new ArrayList<>();

        leaveList.forEach(leave -> eidList.add(leave.getEid()));

        return eidList;
    }

    @Override
    public List<String> getEmployeesOnLeave(Date start, Date end) {
        return null;
    }
}
