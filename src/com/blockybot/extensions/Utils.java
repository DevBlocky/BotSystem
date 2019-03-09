package com.blockybot.extensions;

import com.blockybot.exceptions.ExceptionHelper;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utilities used for BlockyBot
 *
 * @author BlockBa5her (C) BlockBa5her
 */
public class Utils {

    public static class StatusInfo {
        private String internal;
        private String embedReply;

        StatusInfo(@Nullable String internal, @NotNull String embedReply) {
            this.internal = internal;
            this.embedReply = embedReply;
        }

        public String getInternal() {
            return internal;
        }

        public String getEmbedReply() {
            return embedReply;
        }
    }

    /**
     * Making sure the class cannot be initiated
     */
    private Utils() {
    }

    /**
     * Returns the uptime from the "since" input to now
     * 
     * @param since
     *            The time to calculate the uptime from
     * @return A float array of the uptime:
     *
     *         [millis, seconds, minutes, hours, days, weeks]
     */
    public static long[] calcUptime(Instant since) {
        Instant uptime = Instant.now().minusMillis(since.toEpochMilli());
        long totalMillis = uptime.toEpochMilli();

        // taking millis to uptime format
        double weeksB = (double) totalMillis / (1000 * 60 * 60 * 24 * 7); // number of weeks in millis
        double weeks = Math.floor(weeksB);
        double weeksA = weeksB - weeks;

        double daysB = weeksA * 7;
        double days = Math.floor(daysB);
        double daysA = daysB - days;

        double hoursB = daysA * 24;
        double hours = Math.floor(hoursB);
        double hoursA = hoursB - hours;

        double minutesB = hoursA * 60;
        double minutes = Math.floor(minutesB);
        double minutesA = minutesB - minutes;

        double secondsB = minutesA * 60;
        double seconds = Math.floor(secondsB);
        double secondsA = secondsB - seconds;

        double millisB = secondsA * 1000;
        double millis = Math.floor(millisB);

        // returning as new array
        return new long[] { (long) millis, (long) seconds, (long) minutes, (long) hours, (long) days, (long) weeks };
    }

    public static Map<String, StatusInfo> getPossibleStatuses() {
        Map<String, StatusInfo> possible = new LinkedHashMap<>();

        possible.put("online", new StatusInfo("online", "You have set the status to `ONLINE`"));
        possible.put("idle", new StatusInfo("idle", "You have set the status to `IDLE`"));
        possible.put("dnd", new StatusInfo("do_not_disturb", "You have set the status to `DO NOT DISTURB`"));
        possible.put("offline", new StatusInfo("invisible", "You have set the status to `OFFLINE`"));
        possible.put("config", new StatusInfo(null, "You have set the status to `CONFIG DEFAULT`"));

        return possible;
    }

    public static Thread createTimeout(Runnable r, int waitMillis, String name) {
        Thread t = new Thread(() -> { // new thread
            try {
                Thread.sleep(waitMillis); // wait for amount of seconds
                r.run(); // run the timeout
            } catch (InterruptedException e) {
                ExceptionHelper.throwException(e); // throw exception to end program
            }
        });
        t.start(); // start thread
        return t; // return thread
    }

    public static Thread createTimeout(Runnable r, int waitMillis) {
        return createTimeout(r, waitMillis, "Timeout-Thread");
    }
}
