package spreadsheet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpreadsheetIntegrationTest {

    @Test
    void formulaDependsOnFormula_RecalculatesTransitively() {
        Spreadsheet sheet = new Spreadsheet();

        sheet.setCell(new Coordinate(1, 1), new NumericContent("10"));           // A1 = 10
        sheet.setCell(new Coordinate(2, 1), new FormulaContent("=A1+5"));        // A2 = 15
        sheet.setCell(new Coordinate(3, 1), new FormulaContent("=A2*2"));        // A3 = 30

        assertTrue(sheet.getDisplayValue(new Coordinate(3, 1)).contains("-> 30.0"));

        // Change A1 -> 20; expect A2=25 and A3=50
        sheet.setCell(new Coordinate(1, 1), new NumericContent("20"));

        assertTrue(sheet.getDisplayValue(new Coordinate(2, 1)).contains("-> 25.0"));
        assertTrue(sheet.getDisplayValue(new Coordinate(3, 1)).contains("-> 50.0"));
    }

    @Test
    void multipleDependentsUpdateWhenSourceChanges() {
        Spreadsheet sheet = new Spreadsheet();

        sheet.setCell(new Coordinate(1, 1), new NumericContent("3"));         // A1=3
        sheet.setCell(new Coordinate(1, 2), new FormulaContent("=A1+1"));      // B1=4
        sheet.setCell(new Coordinate(1, 3), new FormulaContent("=A1+2"));      // C1=5

        assertTrue(sheet.getDisplayValue(new Coordinate(1, 2)).contains("-> 4.0"));
        assertTrue(sheet.getDisplayValue(new Coordinate(1, 3)).contains("-> 5.0"));

        sheet.setCell(new Coordinate(1, 1), new NumericContent("10"));

        assertTrue(sheet.getDisplayValue(new Coordinate(1, 2)).contains("-> 11.0"));
        assertTrue(sheet.getDisplayValue(new Coordinate(1, 3)).contains("-> 12.0"));
    }

    @Test
    void circularReferenceDetected_TwoCellLoop() {
        Spreadsheet sheet = new Spreadsheet();

        sheet.setCell(new Coordinate(1, 1), new FormulaContent("=B1")); // A1 -> B1
        sheet.setCell(new Coordinate(1, 2), new FormulaContent("=A1")); // B1 -> A1

        // getDisplayValue should show an ERROR (you catch exceptions and format)
        String a1 = sheet.getDisplayValue(new Coordinate(1, 1));
        assertTrue(a1.contains("ERROR") || a1.contains("Circular"),
                "Expected circular reference error, got: " + a1);
    }

    @Test
    void circularReferenceDetected_SelfReference() {
        Spreadsheet sheet = new Spreadsheet();
        sheet.setCell(new Coordinate(1, 1), new FormulaContent("=A1+1"));

        String a1 = sheet.getDisplayValue(new Coordinate(1, 1));
        assertTrue(a1.contains("ERROR") || a1.contains("Circular"),
                "Expected self circular ref error, got: " + a1);
    }
}
