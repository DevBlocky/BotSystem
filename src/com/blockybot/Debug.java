package com.blockybot;

public class Debug {
    public static void trace(Object obj) {
        if (Main.COMMAND_LINE.hasOption("d")) {
            System.out.println(obj);
        }
    }
}
