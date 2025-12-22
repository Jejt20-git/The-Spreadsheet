package spreadsheet;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static spreadsheet.BinaryOp.Operator.*;

class EvaluatorTest {

    @Test
    void testSimpleAddition() {
        Evaluator eval = new Evaluator();

        // 1 + 2 = 3
        Expression e =
                new BinaryOp(
                        ADD,
                        new Literal(1),
                        new Literal(2)
                );

        assertEquals(3.0, eval.evaluate(e), 1e-9);
    }

    @Test
    void testMixedOperators() {
        Evaluator eval = new Evaluator();

        // 3*5 + 4 = 19
        Expression e =
                new BinaryOp(
                        ADD,
                        new BinaryOp(MUL, new Literal(3), new Literal(5)),
                        new Literal(4)
                );

        assertEquals(19.0, eval.evaluate(e), 1e-9);
    }

    @Test
    void testParenthesesViaTree() {
        Evaluator eval = new Evaluator();

        // 3 * (5 + 4) = 27
        Expression e =
                new BinaryOp(
                        MUL,
                        new Literal(3),
                        new BinaryOp(ADD, new Literal(5), new Literal(4))
                );

        assertEquals(27.0, eval.evaluate(e), 1e-9);
    }

    @Test
    void testDivisionByZero() {
        Evaluator eval = new Evaluator();

        Expression e =
                new BinaryOp(
                        DIV,
                        new Literal(5),
                        new Literal(0)
                );

        assertThrows(ArithmeticException.class, () -> eval.evaluate(e));
    }

    @Test
    void testMax() {
        Spreadsheet sheet = new Spreadsheet();
        Evaluator eval = new Evaluator();

        Expression e = new FunctionCall("MAX", java.util.List.of(
                new Literal(1),
                new Literal(5),
                new Literal(3)
        ));

        assertEquals(5.0, eval.evaluate(e, sheet), 1e-9);
    }

    @Test
    void testMin() {
        Spreadsheet sheet = new Spreadsheet();
        Evaluator eval = new Evaluator();

        Expression e = new FunctionCall("MIN", java.util.List.of(
                new Literal(7),
                new Literal(2),
                new Literal(9)
        ));

        assertEquals(2.0, eval.evaluate(e, sheet), 1e-9);
    }
    @Test
    void testSumOverRange() {
        Spreadsheet sheet = new Spreadsheet();
        Evaluator eval = new Evaluator();

        // A1=1, A2=2, A3=3
        sheet.setCell(new Coordinate(1, 1), new NumericContent("1"));
        sheet.setCell(new Coordinate(2, 1), new NumericContent("2"));
        sheet.setCell(new Coordinate(3, 1), new NumericContent("3"));

        Expression e = new FunctionCall("SUM", List.of(
                new RangeRef(new Coordinate(1, 1), new Coordinate(3, 1))
        ));

        assertEquals(6.0, eval.evaluate(e, sheet), 1e-9);
    }

    @Test
    void testAverageOverRangeIgnoresEmptyCells() {
        Spreadsheet sheet = new Spreadsheet();
        Evaluator eval = new Evaluator();

        // A1=2, A3=6 (A2 empty)
        sheet.setCell(new Coordinate(1, 1), new NumericContent("2"));
        sheet.setCell(new Coordinate(3, 1), new NumericContent("6"));

        Expression e = new FunctionCall("AVERAGE", List.of(
                new RangeRef(new Coordinate(1, 1), new Coordinate(3, 1))
        ));

        assertEquals(4.0, eval.evaluate(e, sheet), 1e-9);
    }
    @Test
    void testAverageOverAllEmptyRangeThrows() {
        Spreadsheet sheet = new Spreadsheet();
        Evaluator eval = new Evaluator();

        Expression e = new FunctionCall("AVERAGE", List.of(
                new RangeRef(new Coordinate(1, 1), new Coordinate(3, 1))
        ));

        assertThrows(IllegalArgumentException.class, () -> eval.evaluate(e, sheet));
    }

    @Test
    void testSumOver2DRange() {
        Spreadsheet sheet = new Spreadsheet();
        Evaluator eval = new Evaluator();

        // A1=1, B1=2, A2=3, B2=4 => SUM(A1..B2)=10
        sheet.setCell(new Coordinate(1, 1), new NumericContent("1"));
        sheet.setCell(new Coordinate(1, 2), new NumericContent("2"));
        sheet.setCell(new Coordinate(2, 1), new NumericContent("3"));
        sheet.setCell(new Coordinate(2, 2), new NumericContent("4"));

        Expression e = new FunctionCall("SUM", List.of(
                new RangeRef(new Coordinate(1, 1), new Coordinate(2, 2))
        ));

        assertEquals(10.0, eval.evaluate(e, sheet), 1e-9);
    }


    @Test
    void testParsedSumA1A3EvaluatesInSpreadsheet() {
        Spreadsheet sheet = new Spreadsheet();

        sheet.setCell(new Coordinate(1, 1), new NumericContent("1"));
        sheet.setCell(new Coordinate(2, 1), new NumericContent("2"));
        sheet.setCell(new Coordinate(3, 1), new NumericContent("3"));

        sheet.setCell(new Coordinate(1, 2), new FormulaContent("=SUM(A1;A3)"));

        String display = sheet.getDisplayValue(new Coordinate(1, 2));
        assertTrue(display.contains("-> 6.0"));
    }
    @Test
    void testSumRangeRecalculationAfterDependencyChange() {

        Spreadsheet sheet = new Spreadsheet();

        // A1 = 1, A2 = 2, A3 = 3
        sheet.setCell(new Coordinate(1, 1), new NumericContent("1"));
        sheet.setCell(new Coordinate(2, 1), new NumericContent("2"));
        sheet.setCell(new Coordinate(3, 1), new NumericContent("3"));

        // B1 = SUM(A1;A3)
        sheet.setCell(new Coordinate(1, 2), new FormulaContent("=SUM(A1;A3)"));

        // Initial evaluation
        String before = sheet.getDisplayValue(new Coordinate(1, 2));
        assertTrue(before.contains("6.0"), "Expected SUM to be 6.0");

        // Change A2 from 2 → 20
        sheet.setCell(new Coordinate(2, 1), new NumericContent("20"));

        // Re-evaluation (should update automatically)
        String after = sheet.getDisplayValue(new Coordinate(1, 2));
        assertTrue(after.contains("24.0"), "Expected SUM to update to 24.0");
    }


}