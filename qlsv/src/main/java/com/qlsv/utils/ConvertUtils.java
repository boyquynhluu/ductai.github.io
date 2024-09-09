package com.qlsv.utils;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

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
}
