package com.sliit.se.HR_Spring.controller;

import com.sliit.se.HR_Spring.helper.Utils;
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
import java.util.Map;

@RestController
@RequestMapping("/attendance")
@CrossOrigin(origins = "http://localhost:3000")
public class AttendanceController {

    @Autowired
    private AttendanceServiceImpl attendanceService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity getAllEntries() {

        return new ResponseEntity(Utils.getResponseJsonString(true, attendanceService.getAllEntries()), HttpStatus.OK);
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

                return new ResponseEntity(Utils.getResponseJsonString(true, attendanceService.getAllAttendanceOfEmployeeDuring(start, end, eid)), HttpStatus.OK);
            }
            catch (ParseException pe) {
                pe.printStackTrace();
                return new ResponseEntity(Utils.getResponseJsonString(false, "Error parsing dates, make sure they are in yyyy-mm-dd format; example: 2018-09-30"), HttpStatus.BAD_REQUEST);
            }
        }
        else {
            // if no start and end is specified, we will return all the entries for that employee.
            return new ResponseEntity(Utils.getResponseJsonString(true, attendanceService.getAllAttendanceOfEmployee(eid)), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "emp/{eid}/days", method = RequestMethod.GET)
    public ResponseEntity attendedDaysDuringMonth(@PathVariable("eid") String eid, @RequestParam Map<String, String> query) {
        if (query.containsKey("year") && query.containsKey("month")) {
            int year = Integer.parseInt(query.get("year"));
            int month = Integer.parseInt(query.get("month"));

            return new ResponseEntity(Utils.getResponseJsonString(true, attendanceService.getNumberOfDaysAttendedOnMonth(eid, year, month)), HttpStatus.OK);
        }
        else {
            return new ResponseEntity(Utils.getResponseJsonString(false, "Year and month should be specified in the URL"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "emp/{eid}/checkin", method = RequestMethod.POST)
    public ResponseEntity addEntry(@PathVariable("eid") String eid, @RequestBody Attendance a) {

        Attendance existing = attendanceService.getAttendanceOfEmployeeOn(eid, a.getDate());
        if (existing == null) {
            System.out.println(existing);
            // since the employee is only checking in, we keep the time_out field empty.
            a.setTime_out("");
            attendanceService.addAttendanceEntry(a);

            return new ResponseEntity(Utils.getResponseJsonString(true, null), HttpStatus.CREATED);
        }

        return new ResponseEntity(Utils.getResponseJsonString(false, "Employee " + eid + " has already checked in for " + a.getDate()), HttpStatus.OK);
    }

    @RequestMapping(value = "emp/{eid}/checkout", method = RequestMethod.POST)
    public ResponseEntity checkout(@PathVariable("eid") String eid, @RequestBody Map<String, Object> payload) {
        if (payload.containsKey("time_out")) {
            // out of all the entries we have for the given user, we take the one that,
            // has an empty time_out field.
            for (Attendance a: attendanceService.getAllAttendanceOfEmployee(eid)) {
                if (a.getTime_out() == null || a.getTime_out().equals("")) {
                    // we create a new object with time_out included, then delete the old one and save the new one.
                    // TODO basically the old entry should be updated with the time_out value, but i can't seem to figuire out a way
                    // TODO to do that, so im adding the old info to a new object, add the time_out to the new object as well, and then
                    // TODO delete the old one from db, add the new one, which is sorta too much work so if a better way is found, please update.
                    Attendance newA = new Attendance();
                    newA.setDate(a.getDate());
                    newA.setEid(a.getEid());
                    newA.setTime_in(a.getTime_in());
                    newA.setTime_out(payload.get("time_out").toString());

                    attendanceService.deleteAttendaceOfEmployeeOn(a.getEid(), a.getDate());     // delete the old entry.
                    attendanceService.addAttendanceEntry(newA);     // add the new entry.

                    return new ResponseEntity(Utils.getResponseJsonString(true, "Employee " + a.getEid() + " checked out."), HttpStatus.OK);
                }
            }

            return new ResponseEntity(Utils.getResponseJsonString(false, "Employee " + eid + " has already checked out"), HttpStatus.OK);
        }

        return new ResponseEntity(Utils.getResponseJsonString(false, "Request body should contain time_out parameter"), HttpStatus.OK);
    }

}
