package spreadsheet;

import spreadsheet.IO.sv2Parser;
import spreadsheet.IO.sv2Serializer;

public class Main {
    public static void main(String[] args) throws Exception {

        Spreadsheet sheet = new Spreadsheet();

        // Set some sample cells (numbers, text, formulas stored as RAW strings)
        sheet.setCell(new Coordinate(1, 1), new TextContent("hello"));
        sheet.setCell(new Coordinate(2, 1), new TextContent("world"));
        sheet.setCell(new Coordinate(3, 1), new NumericContent("123"));     // store as number
        sheet.setCell(new Coordinate(1, 2), new TextContent("example"));

        sheet.setCell(new Coordinate(2, 2), new FormulaContent("=10+10"));
        sheet.setCell(new Coordinate(1, 3), new FormulaContent("=123*10"));
        sheet.setCell(new Coordinate(2, 3), new FormulaContent("=C1+10"));
        sheet.setCell(new Coordinate(3, 3), new FormulaContent("=123*(C1+10)"));
        sheet.setCell(new Coordinate(1, 4), new FormulaContent("=C3-A3+100"));

        // Save to file
        sv2Serializer serializer = new sv2Serializer();
        serializer.saveToFile("example.sv2", sheet);
        System.out.println("Saved to example.sv2");

        // Load into a new sheet
        Spreadsheet loadedSheet = new Spreadsheet();
        sv2Parser parser = new sv2Parser();
        parser.loadFromFile("example.sv2", loadedSheet);

        // Print loaded RAW values (no evaluation!)
        System.out.println("Loaded raw values:");

        printCell(loadedSheet, "A1", new Coordinate(1, 1));
        printCell(loadedSheet, "A2", new Coordinate(2, 1));
        printCell(loadedSheet, "A3", new Coordinate(3, 1));
        printCell(loadedSheet, "B1", new Coordinate(1, 2));
        printCell(loadedSheet, "B2", new Coordinate(2, 2));
        printCell(loadedSheet, "C1", new Coordinate(1, 3));
        printCell(loadedSheet, "C2", new Coordinate(2, 3));
        printCell(loadedSheet, "C3", new Coordinate(3, 3));
        printCell(loadedSheet, "D1", new Coordinate(1, 4));
    }

    private static void printCell(Spreadsheet sheet, String label, Coordinate coordinate) {
        Content c = sheet.getCellContent(coordinate);

        // For tasks 1–2, just show what is stored (raw string)
        System.out.println(label + " = " + c.getRaw());
    }
}
