package app.adie.reservation.utils;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app.adie.reservation.BuildConfig;

/**
 * Created by Adie on 28/04/2016.
 */
public class DateUtils {
    protected static Calendar calendar;
    protected static DateFormat monthNameFormat;
    protected static DateFormat monthShortNameFormat;
    protected static DateFormat weekdayNameFormat;
    protected static DateFormat weekdayShortNameFormat;

    public static Date checkDate(Date date) {
        if (date == null) {
            return new Date();
        }
        return date;
    }

    public static String getStringDate(Date date) {
        return new SimpleDateFormat("yyyy/MM/dd").format(date);
    }

    public static String getStringDate(String format, Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String getStringDate(String strDate) {
        String result = BuildConfig.FLAVOR;
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(strDate);
            result = weekdayNameFormat().format(date) + ", " + getStringDate("dd", date) + " " + monthNameFormat().format(date);
        } catch (Exception e) {

        }
        return result;
    }

    public static String getStringDate(String strDate, String format) {
        String result = BuildConfig.FLAVOR;
        try {
            Date date = new SimpleDateFormat(format).parse(strDate);
            result = weekdayNameFormat().format(date) + ", " + getStringDate("dd", date) + " " + monthNameFormat().format(date) + " " + getStringDate("HH:mm:ss", date);
        } catch (Exception e) {

        }
        return result;
    }

    public static Date getDate(String strDate, String format) {
        try {
            return new SimpleDateFormat(format).parse(strDate);
        } catch (Exception e) {

            return null;
        }
    }

    public static String getStringDate(String strDate, String format, boolean monthly_long) {
        String result = BuildConfig.FLAVOR;
        try {
            Date date = new SimpleDateFormat(format).parse(strDate);
            if (monthly_long) {
                return getStringDate("dd", date) + " " + monthNameFormat().format(date);
            }
            return getStringDate("dd", date) + " " + monthShortNameFormat().format(date);
        } catch (Exception e) {

            return result;
        }
    }

    public static String getDateFormatCard(Date date) {
        return weekdayNameFormat().format(date) + ", " + getStringDate("dd", date) + " " + monthNameFormat().format(date);
    }
    public static String getDateNow(Date date) {
        return getStringDate("dd/MM/yyyy", date);
    }
    public static String getTimeNow(Date date) {
        return getStringDate("HH:mm", date);
    }
    public static Date stringToDateHour(String strDate) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd H.i").parse(strDate);
        } catch (Exception e) {

        }
        return date;
    }
    public static Date stringToDate(String strDate) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(strDate);
        } catch (Exception e) {

        }
        return date;
    }

    public static DateFormat monthNameFormat() {
        return monthNameFormat;
    }

    public static DateFormat monthShortNameFormat() {
        return monthShortNameFormat;
    }

    public static DateFormat weekdayNameFormat() {
        return weekdayNameFormat;
    }

    public static DateFormat weekdayShortNameFormat() {
        return weekdayShortNameFormat;
    }

    public static void setMonthsName(String[] newMonths) {
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        symbols.setMonths(newMonths);
        monthNameFormat = new SimpleDateFormat("MMMM", symbols);
    }

    public static void setShortMonthsName(String[] newShortMonths) {
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        symbols.setShortMonths(newShortMonths);
        monthShortNameFormat = new SimpleDateFormat("MMM", symbols);
    }

    public static void setWeekdaysName(String[] newWeekdays) {
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        symbols.setWeekdays(newWeekdays);
        weekdayNameFormat = new SimpleDateFormat("EEEE", symbols);
    }

    public static void setShortWeekdaysName(String[] newShortWeekdays) {
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        symbols.setShortWeekdays(newShortWeekdays);
        weekdayShortNameFormat = new SimpleDateFormat("EEE", symbols);
    }

    public static Date longToDate(long val) {
        return new Date(1000 * val);
    }

    public static long dateToLong(Date val) {
        return val.getTime() / 1000;
    }

    public static Date today() {
        calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static Date tomorrow() {
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public static Date nextYear() {
        calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        return calendar.getTime();
    }

    public static String[] monthLabels() {
        return new String[]{"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
    }

    public static String[] monthLabelsShort() {
        return new String[]{"Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Novr", "Des"};
    }

    public static String[] weekDayLabels() {
        return new String[]{"#", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"};
    }

    public static String[] weekDayLabelsShort() {
        return new String[]{"#", "Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab"};
    }
}
