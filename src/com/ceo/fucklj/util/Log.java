package com.ceo.fucklj.util;

public class Log {
    public static void e(String tag, String param1, Exception e) {
        e.printStackTrace();
    }
    public static void d(String tag, String param1) {
        System.out.println(param1);
    }
}
