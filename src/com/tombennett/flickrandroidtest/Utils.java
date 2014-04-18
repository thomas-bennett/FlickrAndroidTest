package com.tombennett.flickrandroidtest;

public final class Utils {
    private Utils() {
        // Singleton
    }

    public static boolean isEmpty(final String string) {
        return string == null || string.isEmpty();
    }
}
