package com.sliit.se.HR_Spring.controller;

import com.sliit.se.HR_Spring.helper.Utils;
import com.sliit.se.HR_Spring.service.LeaveServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/leave")
@CrossOrigin(origins = "http://localhost:63343")
public class LeaveController {


    @Autowired
    private LeaveServiceImpl leaveService;

    @RequestMapping(value = "/emp/{eid}/issue", method = RequestMethod.GET)
    public ResponseEntity issueLeave(@PathVariable("eid") String eid, @RequestParam Map<String, String > query) {

        if (query.containsKey("start") && query.containsKey("end")) {
            Date start = Utils.getDateFromString(query.get("start"));
            Date end = Utils.getDateFromString(query.get("end"));

            boolean issued = leaveService.issueLeave(eid, start, end);

            return new ResponseEntity(Utils.getResponseJsonString(true, issued), HttpStatus.OK);
        }

        return new ResponseEntity(Utils.getResponseJsonString(false, "To issue leave, start and end dates should be in the request URL"), HttpStatus.BAD_REQUEST);
    }

}
