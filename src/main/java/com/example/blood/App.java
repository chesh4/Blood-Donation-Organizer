package com.example.blood;

import java.nio.file.Path;

public class App {
    public static void main(String[] args) {
        try {
            Path dataFile = Path.of("blood-donation-cli", "data", "donors.csv");
            DonorRepository repository = new DonorRepository(dataFile);
            repository.load();

            ConsoleUI ui = new ConsoleUI(repository);
            ui.run();
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
} 