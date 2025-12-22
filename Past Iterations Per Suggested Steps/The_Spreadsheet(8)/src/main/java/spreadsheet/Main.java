package spreadsheet;


import spreadsheet.IO.sv2Parser;
import spreadsheet.IO.sv2Serializer;

public class Main {

    public static void main(String[] args) throws Exception {

        Spreadsheet sheet = new Spreadsheet();

        sheet.setCell(new Coordinate(1, 1), new FormulaContent("=B1"));
        sheet.setCell(new Coordinate(1, 2), new FormulaContent("=A1"));

        // ----- Create spreadsheet -----
        sheet.setCell(new Coordinate(1, 1), new TextContent("hello"));
        sheet.setCell(new Coordinate(2, 1), new TextContent("world"));
        sheet.setCell(new Coordinate(3, 1), new NumericContent("=123+D1"));
        sheet.setCell(new Coordinate(1, 2), new TextContent("example"));

        sheet.setCell(new Coordinate(2, 2), new FormulaContent("=10+10"));
        sheet.setCell(new Coordinate(1, 3), new FormulaContent("=((123*10)*6-3)"));
        sheet.setCell(new Coordinate(2, 3), new FormulaContent("=C1+10"));
        sheet.setCell(new Coordinate(3, 3), new FormulaContent("=123*(C1+10)"));
        sheet.setCell(new Coordinate(1, 4), new FormulaContent("=C3-A3+100"));
        sheet.setCell(new Coordinate(2, 4), new FormulaContent("=MAX(6;100;C1)"));
        sheet.setCell(new Coordinate(3, 4), new FormulaContent("=MIN(6;0.3;319)"));
        // ----- Save -----
        sv2Serializer serializer = new sv2Serializer();
        serializer.saveToFile("example.sv2", sheet);
        System.out.println("Saved to example.sv2\n");

        // ----- Load -----
        Spreadsheet loadedSheet = new Spreadsheet();
        sv2Parser parser = new sv2Parser();
        parser.loadFromFile("example.sv2", loadedSheet);

        // ----- Print evaluated values -----
        System.out.println("Loaded values (evaluated where possible):");
        // ---- TEST circular reference (for development only) ----
        loadedSheet.setCell(new Coordinate(1, 1), new FormulaContent("=B1")); // A1
        loadedSheet.setCell(new Coordinate(1, 2), new FormulaContent("=A1")); // B1
// --------------------------------------------------------

        System.out.println("A1 = " + loadedSheet.getDisplayValue(new Coordinate(1, 1)));
        System.out.println("A2 = " + loadedSheet.getDisplayValue(new Coordinate(2, 1)));
        System.out.println("A3 = " + loadedSheet.getDisplayValue(new Coordinate(3, 1)));

        System.out.println("B1 = " + loadedSheet.getDisplayValue(new Coordinate(1, 2)));
        System.out.println("B2 = " + loadedSheet.getDisplayValue(new Coordinate(2, 2)));
        System.out.println("B3 = " + loadedSheet.getDisplayValue(new Coordinate(3, 2)));

        System.out.println("C1 = " + loadedSheet.getDisplayValue(new Coordinate(1, 3)));
        System.out.println("C2 = " + loadedSheet.getDisplayValue(new Coordinate(2, 3)));
        System.out.println("C3 = " + loadedSheet.getDisplayValue(new Coordinate(3, 3)));

        System.out.println("D1 = " + loadedSheet.getDisplayValue(new Coordinate(1, 4)));

        System.out.println("D2 = " + loadedSheet.getDisplayValue(new Coordinate(2, 4)));
        System.out.println("D3 = " + loadedSheet.getDisplayValue(new Coordinate(3, 4)));
    }

}
