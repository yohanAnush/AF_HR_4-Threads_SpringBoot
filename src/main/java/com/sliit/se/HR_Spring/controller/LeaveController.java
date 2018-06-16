package com.sliit.se.HR_Spring.controller;

import com.sliit.se.HR_Spring.helper.Utils;
import com.sliit.se.HR_Spring.model.Leave;
import com.sliit.se.HR_Spring.service.LeaveServiceImpl;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/leave")
@CrossOrigin(origins = "http://localhost:3000")
public class LeaveController {


    @Autowired
    private LeaveServiceImpl leaveService;

    @RequestMapping(value = "/emp/{eid}/issue", method = RequestMethod.GET)
    public ResponseEntity issueLeave(@PathVariable("eid") String eid, @RequestParam Map<String, String > query) {

        if (query.containsKey("start") && query.containsKey("end")) {
            Date start = Utils.getDateFromString(query.get("start"));
            Date end = Utils.getDateFromString(query.get("end"));

            boolean issued = leaveService.issueLeave(eid, start, end);

            if (issued) {
                Leave l = new Leave();
                l.setEid(eid);
                l.setStart(start);
                l.setEnd(end);
                l.setOnLeave(true);

                leaveService.saveLeave(l);
            }

            return new ResponseEntity(Utils.getResponseJsonString(true, issued), HttpStatus.OK);
        }

        return new ResponseEntity(Utils.getResponseJsonString(false, "To issue leave, start and end dates should be in the request URL"), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity getAll() {
        return new ResponseEntity(Utils.getResponseJsonString(true, leaveService.getAllEntries()), HttpStatus.OK);
    }

    @RequestMapping(value = "/date/{date}", method = RequestMethod.GET)
    public ResponseEntity getLeavesOnDate(@PathVariable("date") String date) {
        List<Leave> all = leaveService.getAllEntries();
        List<Leave> specific = new ArrayList<>();

        for (Leave l: all) {
            String leaveDate = new DateTime(l.getStart()).toLocalDate().toString();
            if (leaveDate.equals(date)) { specific.add(l); }
        }

        return new ResponseEntity(Utils.getResponseJsonString(true, specific), HttpStatus.OK);
    }
}
