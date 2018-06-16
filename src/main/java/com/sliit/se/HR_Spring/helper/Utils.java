package com.sliit.se.HR_Spring.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /*
     * Since we are dealing with JSON for both requests and responses, we use this method to get a single JSON string,
     * that can be sent to the client which is in the following format:-
     *      {
     *          "success": boolean,
     *          "message": any data type.
     *      }
     */
    public static String getResponseJsonString(boolean success, Object data) {
        Map<String, Object> resMap = new HashMap<>();

        resMap.put("success", success);
        if (data != null) { resMap.put("data", data); }

        return gson.toJson(resMap);
    }

    /*
     * Returns a Date object without time set to 00:00:00
     *
     * @Param dateStr
     *      date should be in the format of yyyy-MM-dd
     */
    public static Date getDateFromString(String dateStr) {

        return new Date(dateStr);/*
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");     // mm is minutes, MM is month.
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
        */
    }
}
