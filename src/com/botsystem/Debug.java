package com.botsystem;

public class Debug {
    public static void trace(Object obj) {
        if (Main.getCli().hasOption("d")) {
            System.out.println(obj);
        }
    }
}
