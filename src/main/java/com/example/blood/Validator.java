package com.example.blood;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

public final class Validator {
    private static final Set<String> VALID_BLOOD_GROUPS = Set.of(
            "A+","A-","B+","B-","AB+","AB-","O+","O-"
    );

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private Validator() {}

    public static boolean isValidName(String name) {
        return name != null && name.trim().length() >= 2;
    }

    public static boolean isValidAge(int age) {
        return age >= 18 && age <= 65; // typical upper limit
    }

    public static boolean isValidBloodGroup(String bg) {
        return bg != null && VALID_BLOOD_GROUPS.contains(bg.trim().toUpperCase());
    }

    public static boolean isValidContact(String contact) {
        return contact != null && contact.trim().length() >= 6; // simplistic
    }

    public static boolean isValidLocation(String location) {
        return location != null && location.trim().length() >= 2;
    }

    public static LocalDate parseDateOrNull(String input) {
        if (input == null) return null;
        String trimmed = input.trim();
        if (trimmed.isEmpty()) return null;
        try {
            return LocalDate.parse(trimmed, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
} 