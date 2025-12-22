// src/main/java/ui/SpreadsheetUI.java
package ui;

import java.util.Scanner;
import spreadsheet.Spreadsheet;

public class SpreadsheetUI {

    private final Scanner scanner = new Scanner(System.in);
    private final Commands commands;

    public SpreadsheetUI(Spreadsheet sheet) {
        this.commands = new Commands(sheet, scanner);
    }

    public void run() {
        System.out.println("=== Spreadsheet Text UI ===");

        boolean running = true;

        // Startup phase: must choose New or Load before main menu
        while (running) {
            if (!commands.isSessionStarted()) {
                running = startupMenu();
                continue;
            }

            running = mainMenu();
        }

        System.out.println("Goodbye!");
    }

    private boolean startupMenu() {
        System.out.println("""
                -------------------------
                1) New spreadsheet
                2) Load spreadsheet
                0) Exit
                -------------------------
                """);
        System.out.print("Choice: ");

        String choice = scanner.nextLine().trim();

        try {
            switch (choice) {
                case "1" -> commands.newSpreadsheet();
                case "2" -> commands.loadSpreadsheetStartup();
                case "0" -> { return false; }
                default -> System.out.println("Invalid option.\n");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }

        return true;
    }

    private boolean mainMenu() {
        printMainMenu();
        System.out.print("Choice: ");

        String choice = scanner.nextLine().trim();

        try {
            switch (choice) {
                case "1" -> commands.setCell();
                case "2" -> commands.viewCell();
                case "3" -> commands.save();    // Save (only if filename known)
                case "4" -> commands.saveAs();  // Save As (always)
                case "0" -> {
                    if (commands.confirmExitIfDirty()) {
                        return false;
                    }
                }
                default -> System.out.println("Invalid option.\n");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }

        return true;
    }

    private void printMainMenu() {
        String current = commands.getCurrentFilename();
        String statusLine = (current == null)
                ? "Current file: (unsaved)"
                : "Current file: " + current;

        String dirtyLine = commands.isDirty()
                ? "Status: UNSAVED changes"
                : "Status: saved";

        // Show "Save" even if unsaved, but Commands.save() will redirect to SaveAs message.
        System.out.println("""
                -------------------------
                %s
                %s
                1) Set cell
                2) View cell
                3) Save
                4) Save As
                0) Exit
                -------------------------
                """.formatted(statusLine, dirtyLine));
    }
}
