package com.qlsv.utils;

import java.util.Objects;

import com.qlsv.constants.Constants;

public class DBUtils {

    public static Object checkBlank(Object obj) {
        if (Objects.isNull(obj) || Constants.BLANK.equals(obj)) {
            return Constants.BLANK;
        } else {
            return obj;
        }
    }
}
