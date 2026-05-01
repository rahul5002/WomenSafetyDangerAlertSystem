package com.womensafety.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger — thread-safe singleton for application-wide logging.
 */
public class Logger {

    private static Logger instance;
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Logger() {}

    public static synchronized Logger getInstance() {
        if (instance == null) instance = new Logger();
        return instance;
    }

    public void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String color = getColor(level);
        System.out.printf("%s[%s] [%s] %s%s%n", color, timestamp, level, message, "\u001B[0m");
    }

    private String getColor(String level) {
        switch (level.toUpperCase()) {
            case "CRITICAL": return "\u001B[41m\u001B[37m"; // red bg
            case "WARNING":  return "\u001B[33m";           // yellow
            case "ALERT":    return "\u001B[35m";           // magenta
            case "DISPATCH": return "\u001B[36m";           // cyan
            case "ERROR":    return "\u001B[31m";           // red
            case "INFO":     return "\u001B[32m";           // green
            default:         return "\u001B[0m";
        }
    }
}
