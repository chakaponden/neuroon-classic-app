package com.inteliclinic.neuroon.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtils {
    private static final SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    private static Calendar calendar = Calendar.getInstance();

    private DateUtils() {
    }

    public static int getDayOfWeekAsIndex() {
        return (Calendar.getInstance().get(7) + 5) % 7;
    }

    public static int getNextDayOfWeekAsIndex() {
        return (Calendar.getInstance().get(7) + 6) % 7;
    }

    public static Date timeInUtc(Integer totalSeconds) {
        int hours = totalSeconds.intValue() / 3600;
        int minutes = totalSeconds.intValue() - (hours * 3600);
        calendar.set(1970, 1, 1, hours, minutes, (totalSeconds.intValue() - (hours * 3600)) - (minutes * 60));
        return calendar.getTime();
    }

    public static boolean sameMonth(Date date1, Date date2) {
        calendar.setTime(date1);
        int month1 = calendar.get(2);
        calendar.setTime(date2);
        return month1 == calendar.get(2) && Math.abs(date1.getTime() - date2.getTime()) < 2764800000L;
    }

    public static boolean sameDate(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal.setTime(date1);
        cal2.setTime(date2);
        if (cal.get(1) == cal2.get(1) && cal.get(6) == cal2.get(6)) {
            return true;
        }
        return false;
    }

    public static int getDayOfMonth(Date date) {
        calendar.setTime(date);
        return calendar.get(5);
    }

    public static boolean in24hFromNow(Date date) {
        long diff = (date.getTime() - new Date().getTime()) / 1000;
        return diff > 0 && diff <= 86400;
    }

    public static int getHoursTo(Date date) {
        return (int) ((date.getTime() - new Date().getTime()) / 3600000);
    }

    public static Date dateAddSeconds(Date date, int interval) {
        calendar.setTime(date);
        calendar.add(13, interval);
        return calendar.getTime();
    }

    public static short minutesTo(Date date) {
        return (short) (secondsInterval(new Date(), date) / 60);
    }

    public static int secondsInterval(Date date1, Date date2) {
        return (int) ((date2.getTime() - date1.getTime()) / 1000);
    }

    public static int secondsToNow(Date date) {
        return secondsInterval(date, new Date());
    }

    public static int compareTo(Date date, Date date1) {
        return date.compareTo(date1);
    }

    public static int compareToNow(Date date) {
        return date.compareTo(new Date());
    }

    public static Date resetDateToToday(Date date) {
        Calendar instance = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(instance.get(1), instance.get(2), instance.get(5));
        return calendar.getTime();
    }

    public static Date dateWithoutTime(Date startDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    public static boolean isToday(Date date) {
        return dateWithoutTime(date).getTime() == dateWithoutTime(new Date()).getTime();
    }

    public static String formatISO8601(Date date) {
        return ISO8601DATEFORMAT.format(date);
    }

    public static Date parseISO8601(String iso8601string) throws ParseException {
        String s = iso8601string.replace("Z", "+00:00");
        try {
            return ISO8601DATEFORMAT.parse(s.substring(0, 22) + s.substring(23));
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
    }
}
