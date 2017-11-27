package com.galenus.act.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final String shortDateAndTimeStr = "--/--/---- --:--";
    private static final String shortDateStr = "--/--/----";
    private static final String shortTimeStr = "--:--";
    private static final String longDateAndTimeStr = "--- ---, ---- --:--";
    private static final String longDateStr = "--- ---, ----";

    private static final SimpleDateFormat shortDateAndTime = new SimpleDateFormat("dd/MM/YYYY HH:mm");
    private static final SimpleDateFormat sqlDateAndTime = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
    private static final SimpleDateFormat shortDate = new SimpleDateFormat("dd/MM/YYYY");
    private static final SimpleDateFormat shortTime = new SimpleDateFormat("HH:mm:ss");

    private static final SimpleDateFormat longDateAndTime = new SimpleDateFormat("ddd MMM, yyyy HH:mm");
    private static final SimpleDateFormat longDate = new SimpleDateFormat("ddd MMM, yyyy");

    private static final SimpleDateFormat sdfCS2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK);
    private static final SimpleDateFormat dateFormatAll = new SimpleDateFormat("dd-MM HH:mm", Locale.UK);
    private static final SimpleDateFormat dateFormatToday = new SimpleDateFormat("HH:mm", Locale.UK);
    private static final SimpleDateFormat dateFormatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

    //
    // CALENDAR STUFF
    //
    public static Calendar convertServerCalendar(String time) {
        Calendar convertedTime = Calendar.getInstance();

        try {
            convertedTime.setTime(sdfCS2.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedTime;
    }

    public static String convertToServerCalendar(Calendar time) {
        return sdfCS2.format(time.getTime());
    }

    public static String convertToSimpleTime(Calendar time) {
        return dateFormatAll.format(time.getTime());
    }

    public static String convertToTodaysTime(Calendar time) {
        return dateFormatToday.format(time.getTime());
    }

    public static Calendar getCalendarWithoutTime(Calendar target) {
        Calendar newDate = Calendar.getInstance();
        newDate.setLenient(false);
        newDate.setTime(target.getTime());
        newDate.set(Calendar.HOUR_OF_DAY, 0);
        newDate.set(Calendar.MINUTE, 0);
        newDate.set(Calendar.SECOND, 0);
        newDate.set(Calendar.MILLISECOND, 0);
        return newDate;
    }

    public static Calendar getCalendarWithoutTime(int year, int month, int day) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, month, day, 0, 0, 0);
        newDate.set(Calendar.MILLISECOND, 0);
        return newDate;
    }

    public static Calendar getTodayWithoutTime() {
        return getCalendarWithoutTime(Calendar.getInstance());
    }



    //
    // DATE STUFF
    //

    public static Date convertServerDate(String time) {
        return convertServerCalendar(time).getTime();
    }

    public static String convertToTodaysTime(Date time) {
        if (time != null) {
            return dateFormatToday.format(time);
        }
        return "";
    }

    public static String convertToServerDate(Date date) {
        if (date != null) {
            return sdfCS2.format(date);
        }
        return "";
    }

    public static String convertToSimpleTime(Date time) {
        return dateFormatAll.format(time);
    }

    public static String convertToSimpleDate(Date date) {
        return dateFormatDate.format(date);
    }

    public static Date getDateWithoutTime(Date target) {
        Calendar newDate = Calendar.getInstance();
        newDate.setLenient(false);
        newDate.setTime(target);
        newDate.set(Calendar.HOUR_OF_DAY, 0);
        newDate.set(Calendar.MINUTE, 0);
        newDate.set(Calendar.SECOND, 0);
        newDate.set(Calendar.MILLISECOND, 0);
        return newDate.getTime();
    }


    public static String formatDateTime(Date date) {
        if (date != null && !date.equals(minDate())) {
            return shortDateAndTime.format(date);
        } else {
            return shortDateAndTimeStr;
        }
    }

    public static String formatTime(Date date) {
        if (date != null && !date.equals(minDate())) {
            return shortTime.format(date);
        } else {
            return shortTimeStr;
        }
    }

    public static String formatDate(Date date) {
        if (date != null && !date.equals(minDate())) {
            return shortDate.format(date);
        } else {
            return shortDateStr;
        }
    }

    public static String formatDateTimeLong(Date date) {
        if (date != null && !date.equals(minDate())) {
            return longDateAndTime.format(date);
        } else {
            return longDateAndTimeStr;
        }
    }

    public static String formatDateLong(Date date) {
        if (date != null && !date.equals(minDate())) {
            return longDate.format(date);
        } else {
            return longDateStr;
        }
    }


    public static Date now() {
        return new Date(Calendar.getInstance().getTime().getTime());
    }

    public static Date minDate() {
        return new Date(0);
    }

    public static Date sqLiteToDate(String dateTxt) {
        Date date = minDate();
        if (dateTxt != null && !dateTxt.isEmpty()) {
            try {
                java.util.Date d = sqlDateAndTime.parse(dateTxt);
                date = new Date(d.getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public static Date stripTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long time = calendar.getTimeInMillis();
        return new Date(time);
    }

    public static Date stripDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, 0);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        long time = calendar.getTimeInMillis();
        return new Date(time);
    }

    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        long time = calendar.getTimeInMillis();
        return new Date(time);
    }
}
