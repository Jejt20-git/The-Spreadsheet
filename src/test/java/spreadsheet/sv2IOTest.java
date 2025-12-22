package spreadsheet;

import org.junit.jupiter.api.Test;
import spreadsheet.IO.sv2Parser;
import spreadsheet.IO.sv2Serializer;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class sv2IOTest {

    @Test
    void saveWritesRowSemicolonFormat_NoCellRefs() throws Exception {
        Spreadsheet sheet = new Spreadsheet();

        // A1=6, C2=2, B3="TOTAL", C3="=A1+1"
        sheet.setCell(new Coordinate(1, 1), new NumericContent("6"));
        sheet.setCell(new Coordinate(2, 3), new NumericContent("2"));
        sheet.setCell(new Coordinate(3, 2), new TextContent("TOTAL"));
        sheet.setCell(new Coordinate(3, 3), new FormulaContent("=A1+1"));

        Path tmp = Files.createTempFile("sheet", ".sv2");
        tmp.toFile().deleteOnExit();

        new sv2Serializer().saveToFile(tmp.toString(), sheet);

        String text = Files.readString(tmp);

        // Must not contain A1= style
        assertFalse(text.contains("A1="));
        assertFalse(text.contains("B2="));
        assertFalse(text.contains("C3="));

        // Should contain semicolons for columns
        assertTrue(text.contains(";"), "Expected semicolon-separated columns");

        // Basic sanity: the numeric '6' should appear in the file
        assertTrue(text.contains("6"));
    }

    @Test
    void saveThenLoadPreservesCellsAndFormulas() throws Exception {
        Spreadsheet original = new Spreadsheet();

        // Grid:
        // Row1: 6 ; ;  (A1=6)
        // Row2: ; ; 2  (C2=2)
        // Row3: ; TOTAL ; =A1+C2  (B3 text, C3 formula)
        original.setCell(new Coordinate(1, 1), new NumericContent("6"));
        original.setCell(new Coordinate(2, 3), new NumericContent("2"));
        original.setCell(new Coordinate(3, 2), new TextContent("TOTAL"));
        original.setCell(new Coordinate(3, 3), new FormulaContent("=A1+C2"));

        Path tmp = Files.createTempFile("sheet", ".sv2");
        tmp.toFile().deleteOnExit();

        new sv2Serializer().saveToFile(tmp.toString(), original);

        Spreadsheet loaded = new Spreadsheet();
        new sv2Parser().loadFromFile(tmp.toString(), loaded);

        // Non-formula content should match raw
        assertEquals("6", loaded.getDisplayValue(new Coordinate(1, 1)));
        assertEquals("2", loaded.getDisplayValue(new Coordinate(2, 3)));
        assertEquals("TOTAL", loaded.getDisplayValue(new Coordinate(3, 2)));

        // Formula should evaluate correctly
        String c3 = loaded.getDisplayValue(new Coordinate(3, 3));
        assertTrue(c3.contains("=A1+C2"), "Expected formula raw preserved");
        assertTrue(c3.contains("-> 8.0"), "Expected evaluated value 8.0, got: " + c3);
    }
}
