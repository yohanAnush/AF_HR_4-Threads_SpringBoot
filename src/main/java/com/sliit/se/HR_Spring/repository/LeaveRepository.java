package com.sliit.se.HR_Spring.repository;

import com.sliit.se.HR_Spring.model.Leave;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface LeaveRepository extends MongoRepository<Leave, String> {

    // get the leave history of a given employee.
    List<Leave> findAllByEid(String eid);

    // get issued leaves the starts on a given date.
    List<Leave> findAllByStart(Date start);

    // get issued leaves that falls within a given time period.
    // we can  basically give the same date for both params.
    List<Leave> findAllByStartAfterAndEndBefore(Date start, Date end);
}
