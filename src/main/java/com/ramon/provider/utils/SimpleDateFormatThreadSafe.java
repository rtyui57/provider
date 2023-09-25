package com.ramon.provider.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SimpleDateFormatThreadSafe {

    protected final SimpleDateFormat df;

    public SimpleDateFormatThreadSafe(String format) {
        df = new SimpleDateFormat(format);
    }

    public synchronized void setTimeZone(TimeZone tz) {
        df.setTimeZone(tz);
    }

    public synchronized String format(Date date) {
        return df.format(date);
    }

    public synchronized Date parse(String source) throws ParseException {
        return df.parse(source);
    }
}
