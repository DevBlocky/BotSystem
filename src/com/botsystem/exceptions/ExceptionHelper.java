package com.botsystem.exceptions;

public class ExceptionHelper {

    public static String getFullExceptionString(Throwable e) {
        String end = "";

        StackTraceElement[] stack = e.getStackTrace();
        end += e.getClass().getTypeName() + ": \"" + e.getMessage() + "\"\n";
        for (StackTraceElement trace : stack) {
            end += "    at " + trace.toString() + "\n";
        }
        if (e.getCause() != null) {
            end += "Cause Exception: " + getFullExceptionString(e.getCause());
        }

        return end;
    }

    public static void throwException(Throwable e) {
        throw new RuntimeException(e);
    }
    public static Thread createExceptionThrowThread(Throwable e) {
        Thread t = new Thread(() -> throwException(e));
        t.setName(e.getClass().getSimpleName() + "ThrowThread");
        t.start();

        return t;
    }
}
