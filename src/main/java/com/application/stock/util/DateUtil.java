package com.application.stock.util;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by sasiddi on 3/19/17.
 */
public class DateUtil {
    public static Date stringToDate(String dateString) {
        if (StringUtils.isEmpty(dateString)) {
            return null;
        }
        try {
            return DoorDashConstants.API_DATE_PARAM_FORMATTER.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateToString(Date date) {
        if (date == null) {
            return "";
        }
        return DoorDashConstants.API_DATE_PARAM_FORMATTER.format(date);
    }
}
