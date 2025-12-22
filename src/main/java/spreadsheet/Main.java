package spreadsheet;

// import parser.ExpressionBuilder;
import spreadsheet.IO.sv2Parser;
import spreadsheet.IO.sv2Serializer;

public class Main {

    //-------------------------------------------------------------------
//    // ---------------- USER INTERFACE IMPLEMENTATION (Comment/Uncomment for UI use) ------------------------------------
//    public static void main(String[] args) {
//        Spreadsheet sheet = new Spreadsheet();
//        ui.SpreadsheetUI ui = new ui.SpreadsheetUI(sheet);
//        ui.run();
//    }

    // -----------------------------------------------------------
    // ----------------------- No USER INTERFACE IMPLEMENTED (Comment out if want UI) ---------------------------
    public static void main(String[] args) throws Exception {

        Spreadsheet sheet = new Spreadsheet();

        //sheet.setCell(new Coordinate(1, 1), new FormulaContent("=B1"));
        //sheet.setCell(new Coordinate(1, 2), new FormulaContent("=A1"));

        // ----- Create spreadsheet -----
        sheet.setCell(new Coordinate(1, 1), new NumericContent("6"));
        sheet.setCell(new Coordinate(2, 1), new NumericContent("15"));
        sheet.setCell(new Coordinate(3, 1), new FormulaContent("=123+D1"));
        sheet.setCell(new Coordinate(1, 2), new TextContent("example"));

        sheet.setCell(new Coordinate(2, 2), new FormulaContent("=10+10"));
        sheet.setCell(new Coordinate(1, 3), new FormulaContent("=((123*10)*6-3)"));
        sheet.setCell(new Coordinate(2, 3), new FormulaContent("=C1+10"));
        sheet.setCell(new Coordinate(3, 3), new FormulaContent("=123*(C1+10)"));
        sheet.setCell(new Coordinate(1, 4), new FormulaContent("=C3-A3+100"));
        sheet.setCell(new Coordinate(2, 4), new FormulaContent("=MAX(6;100;C1)"));
        sheet.setCell(new Coordinate(3, 4), new FormulaContent("=MIN(A1;A2)"));

        // Formula using range
        sheet.setCell(new Coordinate(5, 5), new FormulaContent("=SUM(A1;A2)")); // B1

        // ----- Save -----
        sv2Serializer serializer = new sv2Serializer();
        serializer.saveToFile("Test.sv2", sheet);
        System.out.println("Saved to Test.sv2\n");

        // ----- Load -----
        Spreadsheet loadedSheet = new Spreadsheet();
        sv2Parser parser = new sv2Parser();
        parser.loadFromFile("Test.sv2", loadedSheet);

        // ----- Print evaluated values -----
        System.out.println("Loaded values (evaluated where possible):");
        //-----------------------------------------------------

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


        System.out.println("E5 = " + sheet.getDisplayValue(new Coordinate(5, 5)));

// Change a dependency
        sheet.setCell(new Coordinate(2, 1), new NumericContent("50"));  // A2 updated

        System.out.println("E5 after A2 change = " + sheet.getDisplayValue(new Coordinate(5, 5)));
    }

}
