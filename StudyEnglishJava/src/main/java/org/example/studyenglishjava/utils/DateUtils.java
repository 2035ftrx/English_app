package org.example.studyenglishjava.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String convertTimestampToDateString(long timestamp) {
        // Convert Unix timestamp (milliseconds) to Instant object
        Instant instant = Instant.ofEpochMilli(timestamp);

        // Format to specified date string format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = instant.atZone(ZoneId.systemDefault()).format(formatter);

        return formattedDate;
    }
}