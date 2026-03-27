package com.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class ValidateUtil {

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^0\\d{9}$";
        return phoneNumber != null && phoneNumber.matches(phoneRegex);
    }

    public static boolean isNumber(String value) {
        return value != null && value.matches("\\d+");
    }

    public static boolean isValidDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("dd/MM/yyyy")
                    .withResolverStyle(ResolverStyle.SMART);

            LocalDate.parse(date.trim(), formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
