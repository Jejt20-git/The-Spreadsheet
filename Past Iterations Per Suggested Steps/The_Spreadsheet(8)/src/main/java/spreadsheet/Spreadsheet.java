package spreadsheet;

import parser.ExpressionBuilder;

import java.util.HashMap;
import java.util.Map;

// Spreadsheet stores cells and their raw contents
public class Spreadsheet {

    private final Map<Coordinate, Cell> cells = new HashMap<>();

    public void setCell(Coordinate coordinate, Content content) {
        cells.put(coordinate, new Cell(coordinate, content));
    }

    public Content getCellContent(Coordinate coordinate) {
        Cell cell = cells.get(coordinate);
        return (cell != null) ? cell.getContent() : null;
    }

    public Map<Coordinate, Cell> getCells() {
        return cells;
    }

    public String getDisplayValue(Coordinate coordinate) {
        Content content = getCellContent(coordinate);

        if (content == null) {
            return "<empty>";
        }

        // Non-formula to show raw
        if (!(content instanceof FormulaContent)) {
            return content.getRaw();
        }

        // Formula to parse & evaluate
        String raw = content.getRaw(); // e.g. "=10+10"
        String exprText = raw.startsWith("=") ? raw.substring(1) : raw;

        try {
            ExpressionBuilder builder = new ExpressionBuilder(new SpreadsheetFactory());
            builder.buildExpression(exprText);

            Expression expr = (Expression) builder.getExpression();
            Evaluator evaluator = new Evaluator();
            double value = evaluator.evaluate(expr, this);

            return raw + " -> " + value;

        } catch (Exception e) {
            return raw + " -> ERROR: " + e.getMessage();
        }
    }
}
