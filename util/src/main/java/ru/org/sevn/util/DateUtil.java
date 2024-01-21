package ru.org.sevn.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {
    public static Long ms (LocalDate ld) {
        if (ld == null) {
            return null;
        }
        return ld.atStartOfDay ().toInstant (OffsetDateTime.now ().getOffset ()).toEpochMilli ();
    }

    public static LocalDateTime getLocalDateTime (Long longValue) {
        if (longValue == null) {
            return null;
        }
        return LocalDateTime.ofInstant (Instant.ofEpochMilli (longValue), ZoneId.systemDefault ());
    }

    public static LocalDate getLocalDate (Long longValue) {
        if (longValue == null) {
            return null;
        }
        return Instant.ofEpochMilli (longValue).atZone (ZoneId.systemDefault ()).toLocalDate ();
    }

    public static String today () {
        return day (System.currentTimeMillis ());
    }

    public static String day (LocalDate ld) {
        return day (ms (ld));
    }

    public static String day (Long ms) {
        if (ms == null) {
            return null;
        }
        return new SimpleDateFormat ("dd.MM.yyyy").format (new Date (ms));
    }
}
