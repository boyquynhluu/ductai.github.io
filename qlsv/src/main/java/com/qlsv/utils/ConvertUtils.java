package com.qlsv.utils;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Objects;

import org.springframework.format.datetime.DateFormatter;

import com.qlsv.constants.Constants;

import jakarta.transaction.SystemException;

public class ConvertUtils {

    /**
     * Convert sex from boolean
     * 
     * @param sex
     * @return MALE if is TRUE / else return FEMALE
     */
    public static String convertBooleanToString(Boolean sex) {
        if (sex == Boolean.TRUE) {
            return Constants.MALE;
        } else {
            return Constants.FEMALE;
        }
    }

    /**
     * Convert Date to String
     * 
     * @param date
     * @return String date
     * @throws Exception
     */
    public static String convertDateToString(Date date) throws Exception {
        if (Objects.isNull(date)) {
            return Constants.BLANK;
        }

        String dateStr = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat(Constants.DD_MM_YYYY);
            dateStr = dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException("Format Date To String Has Error!");
        }
        return dateStr;
    }

    /**
     * Convert String To Date SQL
     * 
     * @param dateStr
     * @return LocalDate
     * @throws Exception
     */
    public static Date convertStringToDate(String dateStr) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat(Constants.YYYY_MM_DD);
        try {
            java.util.Date dateUtil = dateFormat.parse(dateStr);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateUtil);
            calendar.set(Calendar.HOUR_OF_DAY, 0); // Set hour to 10 AM
            calendar.set(Calendar.MINUTE, 0); // Set minute to 30
            calendar.set(Calendar.SECOND, 0); // Set seconds to 0
            calendar.set(Calendar.MILLISECOND, 0);
            
            java.util.Date date = (java.util.Date) calendar.getTime();

            // Return the java.sql.Date
            return new java.sql.Date(date.getTime());
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            throw new SystemException("Format String To Date SQL Has Error!");
        }
    }

    /**
     * Convert String To Boolean
     * 
     * @param flgSex
     * @return
     */
    public static boolean convertStringToBoolean(String flgSex) {
        if ("MALE".equals(flgSex)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
