// src/main/java/ui/SpreadsheetCommands.java
package ui;

import java.io.IOException;
import java.util.Scanner;

import spreadsheet.*;
import spreadsheet.IO.sv2Parser;
import spreadsheet.IO.sv2Serializer;
import java.util.List;

public class Commands {

    private final Spreadsheet sheet;
    private final Scanner scanner;

    private final sv2Parser parser = new sv2Parser();
    private final sv2Serializer serializer = new sv2Serializer();

    // UI state
    private boolean sessionStarted = false;
    private boolean dirty = false;
    private String currentFilename = null;

    public Commands(Spreadsheet sheet, Scanner scanner) {
        this.sheet = sheet;
        this.scanner = scanner;
    }

    // --- State getters used by UI ---
    public boolean isSessionStarted() {
        return sessionStarted;
    }

    public boolean isDirty() {
        return dirty;
    }

    public String getCurrentFilename() {
        return currentFilename;
    }

    // --- Startup actions ---
    public void newSpreadsheet() {
        // clear all method, could reset the sheet here.
        // For now, assume Main passes a fresh Spreadsheet.
        sessionStarted = true;
        dirty = false;
        currentFilename = null;

        System.out.println("New spreadsheet created.\n");
    }

    public void loadSpreadsheetStartup() throws IOException {
        System.out.print("Enter filename to load (e.g., sheet.sv2): ");
        String filename = scanner.nextLine().trim();

        parser.loadFromFile(filename, sheet);

        sessionStarted = true;
        dirty = false;
        currentFilename = filename;

        System.out.println("Loaded: " + filename + "\n");
    }

    // --- Core commands ---

    public void setCell() {
        System.out.print("Cell reference (e.g., A1): ");
        String ref = scanner.nextLine().trim();

        System.out.print("Content (e.g., 12, hello, =A1+B2): ");
        String raw = scanner.nextLine();

        Coordinate coord = RefParser.parseRef(ref);

        Content content;
        if (raw.startsWith("=")) {
            content = new FormulaContent(raw);
        } else if (raw.matches("-?\\d+(\\.\\d+)?")) {
            content = new NumericContent(raw);
        } else {
            content = new TextContent(raw);
        }

        // Single call that both sets the cell and returns affected cells
        List<Coordinate> affected = sheet.setCellAndGetAffected(coord, content);

        dirty = true;

        System.out.println("Updated " + RefParser.toRef(coord) + ".");

        if (affected.isEmpty()) {
            System.out.println("No dependent cells affected.\n");
        } else {
            System.out.println("Recalculated/affected cells:");
            for (Coordinate dep : affected) {
                System.out.println("  " + RefParser.toRef(dep) + " = " + sheet.getDisplayValue(dep));
            }
            System.out.println();
        }
    }


    public void viewCell() {
        System.out.print("Cell reference (e.g., B2): ");
        String ref = scanner.nextLine().trim();

        Coordinate c = RefParser.parseRef(ref);

        String value = sheet.getDisplayValue(c);
        System.out.println(ref.toUpperCase() + " = " + value + "\n");
    }

    // --- Save vs Save As ---
    public void save() throws IOException {
        if (currentFilename == null) {
            System.out.println("No filename yet. Use Save As first.\n");
            return;
        }

        serializer.saveToFile(currentFilename, sheet);
        dirty = false;

        System.out.println("Saved: " + currentFilename + "\n");
    }

    public void saveAs() throws IOException {
        System.out.print("Enter filename to save as (e.g., out.sv2): ");
        String filename = scanner.nextLine().trim();

        serializer.saveToFile(filename, sheet);

        currentFilename = filename; // typical Save As behaviour
        dirty = false;

        System.out.println("Saved as: " + filename + "\n");
    }

    // --- Exit behaviour ---
    public boolean confirmExitIfDirty() {
        if (!dirty) return true;

        System.out.print("You have unsaved changes. Save before exiting? (y/n): ");
        String ans = scanner.nextLine().trim().toLowerCase();

        if (ans.equals("y") || ans.equals("yes")) {
            try {
                // Save if possible, otherwise go to Save As
                if (currentFilename != null) {
                    save();
                } else {
                    saveAs();
                }
                return true;
            } catch (IOException e) {
                System.out.println("Save failed: " + e.getMessage());
                System.out.println("Exit cancelled.\n");
                return false;
            }
        }

        // If user says no, exit anyway
        return ans.equals("n") || ans.equals("no");
    }
}
