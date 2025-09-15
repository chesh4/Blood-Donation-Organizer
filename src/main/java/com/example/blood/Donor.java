package com.example.blood;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Donor {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private final String id;
    private String fullName;
    private int age;
    private String bloodGroup;
    private String contact;
    private String location;
    private LocalDate lastDonationDate; // can be null if never donated

    public Donor(String id,
                 String fullName,
                 int age,
                 String bloodGroup,
                 String contact,
                 String location,
                 LocalDate lastDonationDate) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.contact = contact;
        this.location = location;
        this.lastDonationDate = lastDonationDate;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getLastDonationDate() {
        return lastDonationDate;
    }

    public void setLastDonationDate(LocalDate lastDonationDate) {
        this.lastDonationDate = lastDonationDate;
    }

    public String toCsvRecord() {
        // Escape commas by wrapping fields with quotes if needed
        // Format: id,fullName,age,bloodGroup,contact,location,lastDonationDate
        String dateStr = lastDonationDate == null ? "" : lastDonationDate.format(DATE_FORMAT);
        return String.join(",",
                escape(id),
                escape(fullName),
                Integer.toString(age),
                escape(bloodGroup),
                escape(contact),
                escape(location),
                escape(dateStr)
        );
    }

    public static Donor fromCsvRecord(String csvLine) {
        String[] parts = CsvUtils.splitCsvLine(csvLine, 7);
        String id = unescape(parts[0]);
        String fullName = unescape(parts[1]);
        int age = Integer.parseInt(parts[2]);
        String bloodGroup = unescape(parts[3]);
        String contact = unescape(parts[4]);
        String location = unescape(parts[5]);
        String dateStr = unescape(parts[6]);
        LocalDate lastDonation = (dateStr == null || dateStr.isBlank()) ? null : LocalDate.parse(dateStr, DATE_FORMAT);
        return new Donor(id, fullName, age, bloodGroup, contact, location, lastDonation);
    }

    private static String escape(String value) {
        if (value == null) return "";
        boolean needsQuotes = value.contains(",") || value.contains("\"") || value.contains("\n");
        String escaped = value.replace("\"", "\"\"");
        return needsQuotes ? "\"" + escaped + "\"" : escaped;
    }

    private static String unescape(String value) {
        if (value == null) return "";
        String v = value;
        if (v.startsWith("\"") && v.endsWith("\"")) {
            v = v.substring(1, v.length() - 1).replace("\"\"", "\"");
        }
        return v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Donor donor = (Donor) o;
        return Objects.equals(id, donor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
} 