package com.example.smartpantry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public enum Status {
        EXPIRED,
        EXPIRING_SOON,
        FRESH,
        UNKNOWN
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            DATE_FORMAT.setLenient(false);
            return DATE_FORMAT.parse(dateStr.trim());
        } catch (ParseException e) {
            return null;
        }
    }

    public static String formatDate(int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, dayOfMonth);
        return DATE_FORMAT.format(cal.getTime());
    }

    public static Integer getDaysRemaining(String dateStr) {
        Date expiry = parseDate(dateStr);
        if (expiry == null) return null;

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        Calendar exp = Calendar.getInstance();
        exp.setTime(expiry);
        exp.set(Calendar.HOUR_OF_DAY, 0);
        exp.set(Calendar.MINUTE, 0);
        exp.set(Calendar.SECOND, 0);
        exp.set(Calendar.MILLISECOND, 0);

        long diffMs = exp.getTimeInMillis() - today.getTimeInMillis();
        return (int) (diffMs / (1000 * 60 * 60 * 24));
    }

    public static Status getExpiryStatus(String dateStr) {
        Integer days = getDaysRemaining(dateStr);
        if (days == null) return Status.UNKNOWN;
        if (days < 0) return Status.EXPIRED;
        if (days <= 3) return Status.EXPIRING_SOON;
        return Status.FRESH;
    }

    public static String getExpiryLabel(String dateStr) {
        Integer days = getDaysRemaining(dateStr);
        if (days == null) {
            return dateStr == null || dateStr.isEmpty() ? "No expiry set" : dateStr;
        }
        if (days < 0) {
            int past = Math.abs(days);
            return past == 1 ? "Expired yesterday" : "Expired " + past + " days ago";
        } else if (days == 0) {
            return "Expires today!";
        } else if (days == 1) {
            return "Expires tomorrow";
        } else if (days <= 3) {
            return "Expiring in " + days + " days";
        } else {
            return "Expires in " + days + " days (" + dateStr + ")";
        }
    }
}
