package com.ramon.provider.utils;

import org.springframework.util.StringUtils;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

public class TimeUtils {

    public static enum EDateFormat {

        UTC_GUION("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{1,3}", "yyyy-MM-dd HH:mm:ss.SSS", TimeZone.getTimeZone("UTC")),
        UTC_BARRA("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{1,3}", "yyyy/MM/dd HH:mm:ss.SSS", TimeZone.getTimeZone("UTC")),
        UTC_ISO_1("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{1,3}(([+-]\\d{2}))", "yyyy-MM-dd'T'HH:mm:ss.SSSX", TimeZone.getTimeZone("UTC")),
        UTC_ISO_2("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{1,3}(([+-]\\d{4}))", "yyyy-MM-dd'T'HH:mm:ss.SSSXX", TimeZone.getTimeZone("UTC")),
        UTC_ISO_3("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{1,3}(([+-]\\d{2}:\\d{2})|Z)", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", TimeZone.getTimeZone("UTC")),
        UTC_NUMERIC("\\d{4}\\d{2}\\d{2}\\d{2}\\d{2}\\d{2}", "yyyyMMddHHmmss", TimeZone.getTimeZone("UTC")),
        UTC_BOS("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{1,3} .+", "yyyy-MM-dd HH:mm:ss.SSS z", TimeZone.getTimeZone("UTC")),
        UTC_BOS2("[0-9]{4}/[0-9]{2}/[0-9]{2}/[0-9]{2}/[0-9]{2}/[0-9]{2}", "yyyy/MM/dd/HH/mm/ss", TimeZone.getTimeZone("UTC")),
        UTC_TIME_LOG("\\d{2} hours, \\d{2} mins, \\d{2} seconds,  \\d{1,3} millis", "HH 'hours', mm 'mins', ss 'seconds', SSS 'millis'", TimeZone.getTimeZone("UTC")),
        LOCAL_GUION("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{1,3}", "yyyy-MM-dd HH:mm:ss.SSS", TimeZone.getDefault()),
        LOCAL_BARRA("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{1,3}", "yyyy/MM/dd HH:mm:ss.SSS", TimeZone.getDefault()),
        LOCAL_AGENTGSE("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}", "dd/MM/yyyy HH:mm:ss", TimeZone.getDefault()),
        LOCAL_ISO_1("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{1,3}(([+-]\\d{2}))", "yyyy-MM-dd'T'HH:mm:ss.SSSX", TimeZone.getDefault()),
        LOCAL_ISO_2("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{1,3}(([+-]\\d{4}))", "yyyy-MM-dd'T'HH:mm:ss.SSSXX", TimeZone.getDefault()),
        LOCAL_ISO_3("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{1,3}(([+-]\\d{2}:\\d{2})|Z)", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", TimeZone.getDefault()),
        LOCAL_ISO_NOTZ("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{1,3}", "yyyy-MM-dd'T'HH:mm:ss.SSS", TimeZone.getDefault()),
        LOCAL_ISO_NOTZ_SEC("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}", "yyyy-MM-dd'T'HH:mm:ss", TimeZone.getDefault()),
        LOCAL_NUMERIC("\\d{4}\\d{2}\\d{2}\\d{2}\\d{2}\\d{2}", "yyyyMMddHHmmss", TimeZone.getDefault());

        protected final String strPatron;
        protected final String strDateFormat;
        protected final TimeZone timeZ;
        protected final Pattern patron;

        private EDateFormat(String strPatron, String strDateFormat, TimeZone timeZ) {
            this.strPatron = strPatron;
            this.strDateFormat = strDateFormat;
            this.timeZ = timeZ;
            this.patron = Pattern.compile(this.strPatron);
        }

        public String getStrDateFormat() {
            return strDateFormat;
        }

        public String getStrPatron() {
            return strPatron;
        }

        public TimeZone getTimeZ() {
            return timeZ;
        }

        public Pattern getPatron() {
            return patron;
        }
    };

    protected static final String PRETTY_TIME_SEPARATOR = ", ";

    protected static final Map<String, SimpleDateFormatThreadSafe> dateFormats = new HashMap<>();

    protected static synchronized SimpleDateFormatThreadSafe getDateFormat(String format, TimeZone tz) {
        String dateFormatKey = tz.getID() + "|" + format;
        SimpleDateFormatThreadSafe dateformat = dateFormats.get(dateFormatKey);
        if (dateformat == null) {
            dateformat = new SimpleDateFormatThreadSafe(format);
            dateformat.setTimeZone(tz);
            dateFormats.put(dateFormatKey, dateformat);
        }
        return dateformat;
    }

    public static String toString(Date date) {
        return toString(date, EDateFormat.UTC_ISO_3);
    }

    public static String toString(Date date, EDateFormat dateFormat) {
        return toString(date, dateFormat.getStrDateFormat(), dateFormat.getTimeZ());
    }

    public static String toString(Date date, String format) {
        return toString(date, format, TimeZone.getTimeZone("UTC"));
    }

    public static String toStringLocal(Date date, String format) {
        return toString(date, format, TimeZone.getDefault());
    }

    public static String toString(Date date, String format, TimeZone tz) {
        if (date == null) {
            return null;
        }
        return getDateFormat(format, tz).format(date);
    }

    public static Timestamp parseDate(String date, EDateFormat dateFormat) throws ParseException {
        return parseDate(date, dateFormat.getStrDateFormat(), dateFormat.getTimeZ());
    }

    public static Timestamp parseDate(String date, String format) throws ParseException {
        return parseDate(date, format, TimeZone.getTimeZone("UTC"));
    }

    public static Timestamp parseDateLocal(String date, String format) throws ParseException {
        return parseDate(date, format, TimeZone.getDefault());
    }

    public static Timestamp parseDate(String date, String format, TimeZone tz) throws ParseException {
        if (date == null) {
            return null;
        }
        return new Timestamp(getDateFormat(format, tz).parse(date).getTime());
    }

    public static Timestamp parseDate(String date) throws ParseException {
        if (date == null || date.isEmpty()) {
            return null;
        }

        Timestamp res = null;

        for (EDateFormat eDatesInOrder : Arrays.asList(EDateFormat.UTC_ISO_1, EDateFormat.UTC_ISO_2, EDateFormat.UTC_ISO_3, EDateFormat.UTC_BOS, EDateFormat.UTC_GUION, EDateFormat.UTC_BARRA, EDateFormat.UTC_NUMERIC, EDateFormat.UTC_BOS2)) {
            if (eDatesInOrder.getPatron().matcher(date).matches()) {
                try {
                    res = parseDate(date, eDatesInOrder);
                    break;
                } catch (Exception ex) {

                }
            }
        }
        if (res == null) {
            throw new ParseException("No se encontró ningún patrón válido para la fecha: " + date, -1);
        }

        return res;
    }

    public static Timestamp parseDateLocal(String date) throws ParseException {
        if (date == null || date.isEmpty()) {
            return null;
        }

        Timestamp res = null;

        for (EDateFormat eDatesInOrder : Arrays.asList(EDateFormat.LOCAL_ISO_1, EDateFormat.LOCAL_ISO_2, EDateFormat.LOCAL_ISO_3, EDateFormat.LOCAL_ISO_NOTZ, EDateFormat.LOCAL_ISO_NOTZ_SEC, EDateFormat.LOCAL_NUMERIC, EDateFormat.LOCAL_GUION, EDateFormat.LOCAL_BARRA, EDateFormat.LOCAL_AGENTGSE)) {

            if (eDatesInOrder.getPatron().matcher(date).matches()) {
                try {
                    res = parseDate(date, eDatesInOrder);
                    break;
                } catch (Exception ex) {

                }
            }
        }
        if (res == null) {
            throw new ParseException("No format found for date: " + date, -1);
        }

        return res;
    }

    public static String toStringUTC(Date timestamp) {
       return timestamp.toString();
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static long getTotalTime(long startTime) {
        return Math.abs(System.currentTimeMillis() - startTime);
    }

    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    public static boolean isValidDate(String date, EDateFormat format) {
        try {
            parseDate(date, format);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

    public static String timeToPretty(long time) {
        StringBuilder prettyTimeSB = new StringBuilder();

        long remaining = time;
        remaining = addToTimePretty(prettyTimeSB, remaining, (1000 * 60 * 60 * 24), "days");
        remaining = addToTimePretty(prettyTimeSB, remaining, (1000 * 60 * 60), "hours");
        remaining = addToTimePretty(prettyTimeSB, remaining, (1000 * 60), "minutes");
        remaining = addToTimePretty(prettyTimeSB, remaining, (1000), "seconds");
        addToTimePretty(prettyTimeSB, remaining, 1, "milliseconds");

        String prettyTime = prettyTimeSB.toString();
        if (prettyTime.endsWith(PRETTY_TIME_SEPARATOR)) {
            prettyTime = prettyTime.substring(0, prettyTime.length() - PRETTY_TIME_SEPARATOR.length());
        }

        if (!StringUtils.hasText(prettyTime)) {
            prettyTime = "0 milliseconds";
        }
        return prettyTime;
    }

    private static long addToTimePretty(StringBuilder sb, long time, long interval, String tag) {
        long remaining = time;
        if (time > 0) {
            if (time >= interval) {
                long n = remaining / interval;
                remaining = remaining % interval;

                if (n > 0) {
                    sb.append(n).append(" ").append(tag).append(PRETTY_TIME_SEPARATOR);
                }
            }
        }
        return remaining;
    }
}
