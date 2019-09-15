package co.ghast.safeguardac.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    public enum TimeUnit {
        FIT, DAYS, HOURS, MINUTES, SECONDS, MILLISECONDS;
    }

    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_DAY = "yyyy-MM-dd";

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    public static long nowlong() {
        return System.currentTimeMillis();
    }

    public static String when(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        return sdf.format(time);
    }

    public static long a(String a) {
        if (a.endsWith("s")) {
            return Long.valueOf(a.substring(0, a.length() - 1)) * 1000L;
        } else if (a.endsWith("m")) {
            return Long.valueOf(a.substring(0, a.length() - 1)) * 60000L;
        } else if (a.endsWith("h")) {
            return Long.valueOf(a.substring(0, a.length() - 1)) * 3600000L;
        } else if (a.endsWith("d")) {
            return Long.valueOf(a.substring(0, a.length() - 1)) * 86400000L;
        } else if (a.endsWith("m")) {
            return Long.valueOf(a.substring(0, a.length() - 1)) * 2592000000L;
        } else if (a.endsWith("y")) {
            return Long.valueOf(a.substring(0, a.length() - 1)) * 31104000000L;
        }

        return -1;
    }

    public static String date() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    public static String getTime(int time) {
        Date timeDiff = new Date(); // compensate for 1h in millis
        timeDiff.setTime(time * 1000);
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");

        return timeFormat.format(timeDiff);
    }

    public static String since(long epoch) {
        return "Took " + convertString(System.currentTimeMillis() - epoch, 1, TimeUnit.FIT) + ".";
    }

    public static double convert(long time, int trim, TimeUnit type) {
        if (type == TimeUnit.FIT) {
            if (time < 60000L) {
                type = TimeUnit.SECONDS;
            } else if (time < 3600000L) {
                type = TimeUnit.MINUTES;
            } else if (time < 86400000L)
                type = TimeUnit.HOURS;
            else {
                type = TimeUnit.DAYS;
            }
        }
        if (type == TimeUnit.DAYS)
            return MathUtil.trim(trim, time / 86400000.0D);
        if (type == TimeUnit.HOURS)
            return MathUtil.trim(trim, time / 3600000.0D);
        if (type == TimeUnit.MINUTES)
            return MathUtil.trim(trim, time / 60000.0D);
        if (type == TimeUnit.SECONDS)
            return MathUtil.trim(trim, time / 1000.0D);
        return MathUtil.trim(trim, time);
    }

    public static String MakeStr(long time) {
        return convertString(time, 1, TimeUnit.FIT);
    }

    public static String MakeStr(long time, int trim) {
        return convertString(time, trim, TimeUnit.FIT);
    }

    public static String convertString(long time, int trim, TimeUnit type) {
        if (time == -1L) {
            return "Permanent";
        }
        if (type == TimeUnit.FIT) {
            if (time < 60000L) {
                type = TimeUnit.SECONDS;
            } else if (time < 3600000L) {
                type = TimeUnit.MINUTES;
            } else if (time < 86400000L)
                type = TimeUnit.HOURS;
            else {
                type = TimeUnit.DAYS;
            }
        }
        if (type == TimeUnit.DAYS)
            return MathUtil.trim(trim, time / 86400000.0D) + " Days";
        if (type == TimeUnit.HOURS)
            return MathUtil.trim(trim, time / 3600000.0D) + " Hours";
        if (type == TimeUnit.MINUTES)
            return MathUtil.trim(trim, time / 60000.0D) + " Minutes";
        if (type == TimeUnit.SECONDS)
            return MathUtil.trim(trim, time / 1000.0D) + " Seconds";
        return MathUtil.trim(trim, time) + " Milliseconds";
    }

    public static boolean elapsed(long from, long required) {
        return System.currentTimeMillis() - from > required;
    }

    public static long elapsed(long starttime) {
        return System.currentTimeMillis() - starttime;
    }

    public static long left(long start, long required) {
        return (required + start) - System.currentTimeMillis();
    }
}
