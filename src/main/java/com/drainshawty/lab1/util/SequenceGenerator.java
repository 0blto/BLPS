package com.drainshawty.lab1.util;

public class SequenceGenerator {
    private static long counter = 0;

    public static synchronized long getNext() {
        return ++counter;
    }
}
