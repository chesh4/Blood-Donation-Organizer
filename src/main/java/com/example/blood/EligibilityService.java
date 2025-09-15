package com.example.blood;

import java.time.LocalDate;
import java.time.Period;

public final class EligibilityService {
    private static final int MIN_GAP_MONTHS = 3;

    private EligibilityService() {}

    public static boolean isEligible(LocalDate lastDonationDate, LocalDate today) {
        if (lastDonationDate == null) return true;
        LocalDate nextDate = nextEligibleDate(lastDonationDate);
        return !today.isBefore(nextDate);
    }

    public static LocalDate nextEligibleDate(LocalDate lastDonationDate) {
        if (lastDonationDate == null) return LocalDate.now();
        return lastDonationDate.plusMonths(MIN_GAP_MONTHS);
    }

    public static String eligibilityMessage(LocalDate lastDonationDate, LocalDate today) {
        if (isEligible(lastDonationDate, today)) {
            return "Eligible to donate now.";
        } else {
            LocalDate next = nextEligibleDate(lastDonationDate);
            Period p = Period.between(today, next);
            int days = p.getDays() + p.getMonths() * 30 + p.getYears() * 365; // approximate
            return "Not eligible yet. Next eligible on " + next + " (in about " + Math.max(days, 1) + " days).";
        }
    }
} 