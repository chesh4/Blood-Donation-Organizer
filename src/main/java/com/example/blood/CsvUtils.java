package com.example.blood;

import java.util.ArrayList;
import java.util.List;

final class CsvUtils {
    private CsvUtils() {}

    static String[] splitCsvLine(String line, int expectedColumns) {
        List<String> result = new ArrayList<>(expectedColumns);
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        current.append('"');
                        i++; // skip escaped quote
                    } else {
                        inQuotes = false;
                    }
                } else {
                    current.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == ',') {
                    result.add(current.toString());
                    current.setLength(0);
                } else {
                    current.append(c);
                }
            }
        }
        result.add(current.toString());
        while (result.size() < expectedColumns) {
            result.add("");
        }
        return result.toArray(new String[0]);
    }
} 