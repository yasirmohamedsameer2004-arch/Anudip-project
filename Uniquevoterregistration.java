import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

public class UniqueVoterRegistration {

    private static final String DEFAULT_DATA_FILE = "voters.txt";
    private HashSet<String> voterSet = new HashSet<>();
    private Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        UniqueVoterRegistration app = new UniqueVoterRegistration();
        app.run();
    }

    private void run() {
        System.out.println("=== UNIQUE VOTER REGISTRATION SYSTEM ===");
        loadOnStartPrompt();

        while (true) {
            printMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> registerSingle();
                case 2 -> registerMultiple();
                case 3 -> checkExists();
                case 4 -> removeVoter();
                case 5 -> viewAll();
                case 6 -> countVoters();
                case 7 -> clearAll();
                case 8 -> importFromFile();
                case 9 -> exportToFile();
                case 10 -> saveAndExit();
                default -> System.out.println("Invalid choice — try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Register single Voter ID");
        System.out.println("2. Register multiple Voter IDs (comma-separated)");
        System.out.println("3. Check if Voter ID is already registered (unique check)");
        System.out.println("4. Remove a Voter ID");
        System.out.println("5. View all registered Voter IDs");
        System.out.println("6. Count registered Voters");
        System.out.println("7. Clear all registrations");
        System.out.println("8. Import Voter IDs from file (" + DEFAULT_DATA_FILE + ")");
        System.out.println("9. Export Voter IDs to file (" + DEFAULT_DATA_FILE + ")");
        System.out.println("10. Save & Exit");
    }

    private void registerSingle() {
        String id = readLine("Enter Voter ID to register: ").trim();
        if (!isValidId(id)) {
            System.out.println("✖ Invalid ID (empty). Try again.");
            return;
        }
        if (voterSet.add(id)) {
            System.out.println("✔ Voter Registered: " + id);
        } else {
            System.out.println("✖ Duplicate! ID already registered.");
        }
    }

    private void registerMultiple() {
        String line = readLine("Enter multiple Voter IDs separated by commas: ");
        String[] parts = line.split(",");
        int added = 0, duplicates = 0, invalid = 0;
        for (String p : parts) {
            String id = p.trim();
            if (!isValidId(id)) { invalid++; continue; }
            if (voterSet.add(id)) added++; else duplicates++;
        }
        System.out.printf("Added: %d | Duplicates skipped: %d | Invalid: %d%n", added, duplicates, invalid);
    }

    private void checkExists() {
        String id = readLine("Enter Voter ID to check: ").trim();
        if (!isValidId(id)) {
            System.out.println("✖ Invalid ID (empty).");
            return;
        }
        if (voterSet.contains(id)) {
            System.out.println("✖ NOT UNIQUE — already registered.");
        } else {
            System.out.println("✔ UNIQUE — not registered yet.");
        }
    }

    private void removeVoter() {
        String id = readLine("Enter Voter ID to remove: ").trim();
        if (voterSet.remove(id)) {
            System.out.println("✔ Removed: " + id);
        } else {
            System.out.println("✖ ID not found.");
        }
    }

    private void viewAll() {
        if (voterSet.isEmpty()) {
            System.out.println("(No registered voters)");
            return;
        }
        System.out.println("Registered Voter IDs:");
        voterSet.forEach(v -> System.out.println("- " + v));
    }

    private void countVoters() {
        System.out.println("Total registered voters: " + voterSet.size());
    }

    private void clearAll() {
        String confirm = readLine("Type 'YES' to clear all registrations: ");
        if ("YES".equalsIgnoreCase(confirm)) {
            voterSet.clear();
            System.out.println("✔ All registrations cleared.");
        } else {
            System.out.println("Aborted. No changes made.");
        }
    }

    private void importFromFile() {
        String path = readLine("Enter filename to import (or press Enter for default): ").trim();
        if (path.isEmpty()) path = DEFAULT_DATA_FILE;
        int added = 0, skipped = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String id = line.trim();
                if (!isValidId(id)) { skipped++; continue; }
                if (voterSet.add(id)) added++; else skipped++;
            }
            System.out.printf("Import complete. Added: %d | Skipped (duplicates/invalid): %d%n", added, skipped);
        } catch (IOException e) {
            System.out.println("✖ Error reading file: " + e.getMessage());
        }
    }

    private void exportToFile() {
        String path = readLine("Enter filename to export (or press Enter for default): ").trim();
        if (path.isEmpty()) path = DEFAULT_DATA_FILE;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (String id : voterSet) {
                bw.write(id);
                bw.newLine();
            }
            System.out.println("✔ Exported " + voterSet.size() + " IDs to " + path);
        } catch (IOException e) {
            System.out.println("✖ Error writing file: " + e.getMessage());
        }
    }

    private void saveAndExit() {
        String save = readLine("Save current registrations to default file before exit? (Y/N): ");
        if (save.equalsIgnoreCase("Y")) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(DEFAULT_DATA_FILE))) {
                for (String id : voterSet) {
                    bw.write(id);
                    bw.newLine();
                }
                System.out.println("✔ Saved to " + DEFAULT_DATA_FILE);
            } catch (IOException e) {
                System.out.println("✖ Error saving file: " + e.getMessage());
            }
        }
        System.out.println("Exiting... Goodbye!");
        sc.close();
        System.exit(0);
    }

 
    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String s = sc.nextLine().trim();
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    private boolean isValidId(String id) {
        return id != null && !id.isEmpty();
    }

    private void loadOnStartPrompt() {
        String resp = readLine("Load existing registrations from default file (" + DEFAULT_DATA_FILE + ")? (Y/N): ");
        if (resp.equalsIgnoreCase("Y")) {
            int before = voterSet.size();
            try (BufferedReader br = new BufferedReader(new FileReader(DEFAULT_DATA_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String id = line.trim();
                    if (isValidId(id)) voterSet.add(id);
                }
                System.out.println("Loaded " + (voterSet.size() - before) + " IDs from " + DEFAULT_DATA_FILE);
            } catch (IOException e) {
                System.out.println("No existing file found or error reading file.");
            }
        }
    }
}
