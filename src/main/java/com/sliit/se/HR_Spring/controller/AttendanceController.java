package com.sliit.se.HR_Spring.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sliit.se.HR_Spring.model.Attendance;
import com.sliit.se.HR_Spring.service.AttendanceServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/attendance")
@CrossOrigin(origins = "http://localhost:63343")
public class AttendanceController {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Autowired
    private AttendanceServiceImpl attendanceService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity getAllEntries() {

        return new ResponseEntity(getResponseJsonString(true, attendanceService.getAllEntries()), HttpStatus.OK);
    }

    /*
     * This method handles two aspects of attendance entries for a given employee.
     *      If a time period is specified, attendance entries of that employee within that period is returned,
     *      else, all the entries for that employee is returned.
     */
    @RequestMapping(value = "/emp/{eid}", method = RequestMethod.GET)
    public ResponseEntity getEmployeeAttendanceForPeriod(@PathVariable("eid") String eid, @RequestParam Map<String, String> query) {
        if (query.containsKey("start") && query.containsKey("end")) {
            try {
                // construct the dates from strings.
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // don't use mm since it means minutes.
                Date start = df.parse(query.get("start"));
                Date end = df.parse(query.get("end"));

                System.out.println(eid);
                System.out.println(query.get("start") + " > " + start);
                System.out.println(query.get("end") + " > " + end);

                return new ResponseEntity(getResponseJsonString(true, attendanceService.getAllAttendanceOfEmployeeDuring(start, end, eid)), HttpStatus.OK);
            }
            catch (ParseException pe) {
                pe.printStackTrace();
                return new ResponseEntity(getResponseJsonString(false, "Error parsing dates, make sure they are in yyyy-mm-dd format; example: 2018-09-30"), HttpStatus.BAD_REQUEST);
            }
        }
        else {
            // if no start and end is specified, we will return all the entries for that employee.
            return new ResponseEntity(getResponseJsonString(true, attendanceService.getAllAttendanceOfEmployee(eid)), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "emp/{eid}/days", method = RequestMethod.GET)
    public ResponseEntity attendedDaysDuringMonth(@PathVariable("eid") String eid, @RequestParam Map<String, String> query) {
        if (query.containsKey("year") && query.containsKey("month")) {
            int year = Integer.parseInt(query.get("year"));
            int month = Integer.parseInt(query.get("month"));

            return new ResponseEntity(getResponseJsonString(true, attendanceService.getNumberOfDaysAttendedOnMonth(eid, year, month)), HttpStatus.OK);
        }
        else {
            return new ResponseEntity(getResponseJsonString(false, "Year and month should be specified in the URL"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity addEntry(@RequestBody Attendance a) {
        attendanceService.addAttendanceEntry(a);

        return new ResponseEntity(getResponseJsonString(true, null), HttpStatus.CREATED);
    }


    /*
     * Since we are dealing with JSON for both requests and responses, we use this method to get a single JSON string,
     * that can be sent to the client which is in the following format:-
     *      {
     *          "success": boolean,
     *          "message": any data type.
     *      }
     */
    private String getResponseJsonString(boolean success, Object data) {
        Map<String, Object> resMap = new HashMap<>();

        resMap.put("success", success);
        if (data != null) { resMap.put("message", data); }

        return gson.toJson(resMap);
    }
}
