package com.bbmsl.payapidemo.util;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class StringUtil {
    private static final String TAG = "StringUtil";

    public static String safeString(String data){
        return data == null ? "" : data;
    }

    public static boolean isEmpty(String data) {return data == null || data.length() == 0;}

    public static String iso8601Date (int secondsFromNow){

        Instant instant = Instant.now();
        instant = instant.plusMillis(secondsFromNow*1000);
        return DateTimeFormatter.ISO_INSTANT.format (instant);

    }

}
