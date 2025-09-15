package com.example.blood;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final DonorRepository repository;
    private final Scanner scanner;

    public ConsoleUI(DonorRepository repository) {
        this.repository = repository;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = prompt("Choose an option: ");
            switch (choice) {
                case "1":
                    handleRegister();
                    break;
                case "2":
                    handleSearch();
                    break;
                case "3":
                    handleCheckEligibility();
                    break;
                case "4":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("=== Blood Donation Organizer ===");
        System.out.println("1) Register a new donor");
        System.out.println("2) Search donors");
        System.out.println("3) Check eligibility for a donor");
        System.out.println("4) Exit");
    }

    private void handleRegister() {
        System.out.println("-- Register New Donor --");
        String name;
        while (true) {
            name = prompt("Full name: ");
            if (Validator.isValidName(name)) break;
            System.out.println("Invalid name. Please enter at least 2 characters.");
        }

        int age;
        while (true) {
            String ageStr = prompt("Age (>=18): ");
            try {
                age = Integer.parseInt(ageStr.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid age. Please enter a number.");
                continue;
            }
            if (Validator.isValidAge(age)) break;
            System.out.println("Age must be between 18 and 65.");
        }

        String bloodGroup;
        while (true) {
            bloodGroup = prompt("Blood group (A+, A-, B+, B-, AB+, AB-, O+, O-): ").toUpperCase();
            if (Validator.isValidBloodGroup(bloodGroup)) break;
            System.out.println("Invalid blood group. Please enter a valid type.");
        }

        String contact;
        while (true) {
            contact = prompt("Contact (phone/email): ");
            if (Validator.isValidContact(contact)) break;
            System.out.println("Invalid contact. Please enter at least 6 characters.");
        }

        String location;
        while (true) {
            location = prompt("Location (city/district): ");
            if (Validator.isValidLocation(location)) break;
            System.out.println("Invalid location. Please enter at least 2 characters.");
        }

        LocalDate lastDonation = null;
        while (true) {
            String dateStr = prompt("Last donation date (YYYY-MM-DD) or leave empty if never: ");
            lastDonation = Validator.parseDateOrNull(dateStr);
            if (dateStr.trim().isEmpty() || lastDonation != null) break;
            System.out.println("Invalid date format. Use YYYY-MM-DD.");
        }

        try {
            Donor donor = repository.add(name.trim(), age, bloodGroup.trim(), contact.trim(), location.trim(), lastDonation);
            boolean eligible = EligibilityService.isEligible(donor.getLastDonationDate(), LocalDate.now());
            System.out.println();
            System.out.println("Donor registered with ID: " + donor.getId());
            System.out.println("Eligibility: " + (eligible ? "Eligible to donate now." : EligibilityService.eligibilityMessage(donor.getLastDonationDate(), LocalDate.now())));
        } catch (Exception e) {
            System.out.println("Failed to register donor: " + e.getMessage());
        }
    }

    private void handleSearch() {
        System.out.println("-- Search Donors --");
        String bloodGroup;
        while (true) {
            bloodGroup = prompt("Required blood group: ").toUpperCase();
            if (Validator.isValidBloodGroup(bloodGroup)) break;
            System.out.println("Invalid blood group. Try again.");
        }
        String location;
        while (true) {
            location = prompt("Location (city/district): ");
            if (Validator.isValidLocation(location)) break;
            System.out.println("Invalid location. Try again.");
        }

        List<Donor> results = repository.search(bloodGroup, location);
        if (results.isEmpty()) {
            System.out.println("No matching donors found.");
            return;
        }

        System.out.println(results.size() + " donor(s) found:");
        for (Donor d : results) {
            boolean eligible = EligibilityService.isEligible(d.getLastDonationDate(), LocalDate.now());
            System.out.println("- " + d.getFullName() + " | Contact: " + d.getContact() + " | Location: " + d.getLocation() +
                    " | Last: " + (d.getLastDonationDate() == null ? "N/A" : d.getLastDonationDate()) +
                    " | Status: " + (eligible ? "Eligible" : "Not eligible, next: " + EligibilityService.nextEligibleDate(d.getLastDonationDate())));
        }
    }

    private void handleCheckEligibility() {
        System.out.println("-- Check Eligibility --");
        String name = prompt("Enter donor full name to check (exact match): ");
        Donor match = repository.getAll().stream()
                .filter(d -> d.getFullName().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElse(null);
        if (match == null) {
            System.out.println("No donor found with that name.");
            return;
        }
        System.out.println(EligibilityService.eligibilityMessage(match.getLastDonationDate(), LocalDate.now()));
    }

    private String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
} 