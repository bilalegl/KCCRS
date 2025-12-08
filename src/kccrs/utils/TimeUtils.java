package kccrs.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String format(LocalDateTime time) {
        return time.format(formatter);
    }

    public static LocalDateTime parse(String timeString) {
        return LocalDateTime.parse(timeString, formatter);
    }
}
