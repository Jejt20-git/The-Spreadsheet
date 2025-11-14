import java.io.File;
import java.util.Scanner;

public class main {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        Spreadsheet sheet;

        //  1. Choose: load existing or new sheet
        System.out.println("Welcome to the Spreadsheet demo!");
        System.out.println("Do you want to:");
        System.out.println("  1) Load a spreadsheet from file");
        System.out.println("  2) Start with a new empty spreadsheet");
        System.out.print("Enter 1 or 2: ");

        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            System.out.print("Enter file name to load (e.g., sheet.sv2): ");
            String filename = scanner.nextLine().trim();

            File f = new File(filename);
            if (f.exists()) {
                sheet = sv2Parser.fromFile(filename);
                System.out.println("Loaded spreadsheet from " + filename + ".\n");
            } else {
                System.out.println("File not found. Starting with a new empty spreadsheet.\n");
                sheet = new Spreadsheet();
            }
        } else {
            sheet = new Spreadsheet();
            System.out.println("Starting with a new empty spreadsheet.\n");
        }

        // ==== 2. Let user enter cell contents ====
        System.out.println("Now you can enter cell contents.");
        System.out.println("Type 'quit' as the cell to finish.\n");

        while (true) {
            System.out.print("Enter cell (e.g., A1) or 'quit': ");
            String cellInput = scanner.nextLine().trim();

            if (cellInput.equalsIgnoreCase("quit")) {
                break;
            }

            System.out.print("Enter content: ");
            String contentInput = scanner.nextLine();

            try {
                Coordinate c = Coordinate.fromA1(cellInput);
                sheet.setContent(c, contentInput);
                System.out.println("✔ Stored " + cellInput + " = " + contentInput + "\n");
            } catch (Exception e) {
                System.out.println(" Error: " + e.getMessage() + "\n");
            }
        }

        // ==== 3. Show final contents ====
        System.out.println("\nFinal spreadsheet contents:");
        if (sheet.cells().isEmpty()) {
            System.out.println("(no non-empty cells)");
        } else {
            sheet.cells().entrySet().stream()
                    .sorted((a, b) -> {
                        int r = Integer.compare(a.getKey().row, b.getKey().row);
                        if (r != 0) return r;
                        return Integer.compare(a.getKey().colIndex, b.getKey().colIndex);
                    })
                    .forEach(e -> System.out.println(e.getKey() + " = " + e.getValue().getContent().raw()));
        }

        // ==== 4. Ask where to save ====
        System.out.print("\nEnter file name to save (or leave empty to skip saving): ");
        String saveName = scanner.nextLine().trim();

        if (!saveName.isEmpty()) {
            sv2Serializer.toFile(sheet, saveName);
            System.out.println("Saved spreadsheet to " + saveName + ".");
        } else {
            System.out.println("Skipped saving.");
        }

        System.out.println("Goodbye!");
    }
}
