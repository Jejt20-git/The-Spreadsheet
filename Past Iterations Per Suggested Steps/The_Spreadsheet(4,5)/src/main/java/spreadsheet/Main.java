package spreadsheet;

import parser.ExpressionBuilder;
import spreadsheet.IO.sv2Parser;
import spreadsheet.IO.sv2Serializer;

public class Main {

    public static void main(String[] args) throws Exception {

        Spreadsheet sheet = new Spreadsheet();

        // ----- Create spreadsheet -----
        sheet.setCell(new Coordinate(1, 1), new TextContent("hello"));
        sheet.setCell(new Coordinate(2, 1), new TextContent("world"));
        sheet.setCell(new Coordinate(3, 1), new NumericContent("123"));
        sheet.setCell(new Coordinate(1, 2), new TextContent("example"));

        sheet.setCell(new Coordinate(2, 2), new FormulaContent("=10+10"));
        sheet.setCell(new Coordinate(1, 3), new FormulaContent("=((123*10)*6-3)"));
        sheet.setCell(new Coordinate(2, 3), new FormulaContent("=C1+10"));
        sheet.setCell(new Coordinate(3, 3), new FormulaContent("=123*(C1+10)"));
        sheet.setCell(new Coordinate(1, 4), new FormulaContent("=C3-A3+100"));

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

        printCellEvaluated(loadedSheet, "A1", new Coordinate(1, 1));
        printCellEvaluated(loadedSheet, "A2", new Coordinate(2, 1));
        printCellEvaluated(loadedSheet, "A3", new Coordinate(3, 1));
        printCellEvaluated(loadedSheet, "B1", new Coordinate(1, 2));
        printCellEvaluated(loadedSheet, "B2", new Coordinate(2, 2));
        printCellEvaluated(loadedSheet, "C1", new Coordinate(1, 3));
        printCellEvaluated(loadedSheet, "C2", new Coordinate(2, 3));
        printCellEvaluated(loadedSheet, "C3", new Coordinate(3, 3));
        printCellEvaluated(loadedSheet, "D1", new Coordinate(1, 4));
    }

    // ------------------------------------------------------------------

    private static void printCellEvaluated(Spreadsheet sheet, String label, Coordinate coordinate) {

        Content content = sheet.getCellContent(coordinate);

        if (content == null) {
            System.out.println(label + " = <empty>");
            return;
        }

        // Non-formula → just print raw
        if (!(content instanceof FormulaContent)) {
            System.out.println(label + " = " + content.getRaw());
            return;
        }

        // Formula → parse & evaluate
        String raw = content.getRaw();              // "=10+10"
        String exprText = raw.substring(1);         // "10+10"

        try {
            ExpressionBuilder builder =
                    new ExpressionBuilder(new SpreadsheetFactory());

            builder.buildExpression(exprText);
            Expression expr = (Expression) builder.getExpression();

            Evaluator evaluator = new Evaluator();
            double value = evaluator.evaluate(expr);

            System.out.println(label + " = " + raw + " -> " + value);

        } catch (Exception e) {
            System.out.println(label + " = " + raw + " -> ERROR: " + e.getMessage());
        }
    }
}
