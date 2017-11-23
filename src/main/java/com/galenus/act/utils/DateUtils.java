package com.galenus.act.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

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

    public static Date now() {
        return Calendar.getInstance().getTime();
    }

    public static Date min() {
        return new Date(0);
    }

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
}
