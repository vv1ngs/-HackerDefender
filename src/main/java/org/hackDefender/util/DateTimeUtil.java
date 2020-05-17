package org.hackDefender.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * @author vvings
 * @version 2020/3/22 1:23
 */
public class DateTimeUtil {

    public static final String STANDER_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String DateToString(Date dateTimeDate, String formatStr) {
        if (dateTimeDate == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(dateTimeDate);
        return dateTime.toString(formatStr);
    }

    public static Date StringToDate(String dateTimeString, String formatStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeString);
        return dateTime.toDate();
    }

    public static String DateToString(Date dateTimeDate) {
        if (dateTimeDate == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(dateTimeDate);
        return dateTime.toString(STANDER_FORMAT);
    }

    public static Date StringToDate(String dateTimeString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDER_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeString);
        return dateTime.toDate();
    }
}
