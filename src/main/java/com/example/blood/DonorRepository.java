package com.example.blood;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class DonorRepository {
    private static final String[] HEADERS = new String[] {
            "id","fullName","age","bloodGroup","contact","location","lastDonationDate"
    };

    private final Path storagePath;

    private final Map<String, Donor> idToDonor = new HashMap<>();
    private final Map<String, List<Donor>> bloodGroupIndex = new HashMap<>();

    public DonorRepository(Path storagePath) {
        this.storagePath = storagePath;
    }

    public void load() throws IOException {
        idToDonor.clear();
        bloodGroupIndex.clear();

        if (!Files.exists(storagePath)) {
            ensureParentDir();
            try (BufferedWriter writer = Files.newBufferedWriter(storagePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
                writer.write(String.join(",", HEADERS));
                writer.newLine();
            }
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(storagePath, StandardCharsets.UTF_8)) {
            String line = reader.readLine(); // header
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                Donor donor = Donor.fromCsvRecord(line);
                addToMemory(donor);
            }
        }
    }

    public synchronized Donor add(String fullName, int age, String bloodGroup, String contact, String location, LocalDate lastDonationDate) throws IOException {
        String id = UUID.randomUUID().toString();
        Donor donor = new Donor(id, fullName, age, bloodGroup, contact, location, lastDonationDate);
        addToMemory(donor);
        appendToFile(donor);
        return donor;
    }

    public Collection<Donor> getAll() {
        return Collections.unmodifiableCollection(idToDonor.values());
    }

    public List<Donor> search(String bloodGroup, String location) {
        List<Donor> list = bloodGroupIndex.getOrDefault(normalizeKey(bloodGroup), Collections.emptyList());
        String normalizedLocation = normalizeKey(location);
        return list.stream()
                .filter(d -> normalizeKey(d.getLocation()).equals(normalizedLocation))
                .collect(Collectors.toList());
    }

    private static String normalizeKey(String s) {
        return s == null ? "" : s.trim().toUpperCase();
    }

    private void addToMemory(Donor donor) {
        idToDonor.put(donor.getId(), donor);
        bloodGroupIndex.computeIfAbsent(normalizeKey(donor.getBloodGroup()), k -> new ArrayList<>()).add(donor);
    }

    private void appendToFile(Donor donor) throws IOException {
        ensureParentDir();
        try (BufferedWriter writer = Files.newBufferedWriter(storagePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(donor.toCsvRecord());
            writer.newLine();
        }
    }

    private void ensureParentDir() throws IOException {
        Path parent = storagePath.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }
} 