package com.womensafety.util;

import java.util.regex.Pattern;

/**
 * InputValidator — validates user inputs (phone, email, password strength).
 */
public class InputValidator {

    private static final Pattern PHONE_PATTERN  = Pattern.compile("^[6-9]\\d{9}$");
    private static final Pattern EMAIL_PATTERN  = Pattern.compile("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");

    private InputValidator() {}

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isNonEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static String sanitize(String input) {
        if (input == null) return "";
        return input.trim().replaceAll("[<>\"'%;()&+]", "");
    }
}
