package com.devserbyn.suncheck.utility;

import com.devserbyn.suncheck.constant.STR_CONSTANT;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtility {

    public static Date convertHoursAndMinToDate(String inputDateStr) {
        DateFormat readFormat = new SimpleDateFormat(STR_CONSTANT.MESSAGE_TIME_FORMAT);
        try {
            return readFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
