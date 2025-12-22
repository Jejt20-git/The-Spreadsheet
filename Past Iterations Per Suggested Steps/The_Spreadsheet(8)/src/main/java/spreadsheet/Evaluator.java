package spreadsheet;

import parser.ExpressionBuilder;

import java.util.HashSet;
import java.util.Set;

public class Evaluator {

    public double evaluate(Expression e) {
        return evaluate(e, new Spreadsheet());
    }
    public double evaluate(Expression e, Spreadsheet sheet) {
        return evaluate(e, sheet, new HashSet<>());
    }

    private double evaluate(Expression e, Spreadsheet sheet, Set<Coordinate> visiting) {

        if (e instanceof Literal lit) {
            return lit.value();
        }

        if (e instanceof BinaryOp b) {
            double left  = evaluate(b.getLeft(), sheet, visiting);
            double right = evaluate(b.getRight(), sheet, visiting);

            return switch (b.getOp()) {
                case ADD -> left + right;
                case SUB -> left - right;
                case MUL -> left * right;
                case DIV -> {
                    if (right == 0.0) throw new ArithmeticException("Division by zero");
                    yield left / right;
                }
            };
        }

        if (e instanceof CellRef c) {
            Coordinate coord = c.toCoordinate();

            //  Loop Detection
            if (visiting.contains(coord)) {
                throw new IllegalStateException("Circular reference detected at " + c.ref());
            }

            visiting.add(coord);
            try {
                Content content = sheet.getCellContent(coord);
                if (content == null) {
                    throw new IllegalArgumentException("Referenced empty cell: " + c.ref());
                }

                return evaluateContentAsNumber(content, sheet, visiting);

            } finally {
                visiting.remove(coord);
            }
        }
        //For function call
        if (e instanceof FunctionCall f) {
            String name = f.getName(); // already uppercase
            java.util.List<Expression> args = f.getArgs();

            if (args.isEmpty()) {
                throw new IllegalArgumentException(name + "() requires at least 1 argument");
            }

            double best = evaluate(args.get(0), sheet, visiting);

            for (int i = 1; i < args.size(); i++) {
                double v = evaluate(args.get(i), sheet, visiting);

                if (name.equals("MAX")) best = Math.max(best, v);
                else if (name.equals("MIN")) best = Math.min(best, v);
                else throw new IllegalArgumentException("Unknown function: " + name);
            }

            return best;
        }

        if (e instanceof ErrorExpression err) {
            throw new IllegalArgumentException(err.message());
        }

        throw new IllegalArgumentException("Unknown expression type: " + e.getClass().getName());
    }

    private double evaluateContentAsNumber(Content content, Spreadsheet sheet, Set<Coordinate> visiting) {

        if (content instanceof NumericContent) {
            return Double.parseDouble(content.getRaw());
        }

        if (content instanceof FormulaContent) {
            String raw = content.getRaw(); // "=C1+10"
            String exprText = raw.startsWith("=") ? raw.substring(1) : raw;

            ExpressionBuilder b = new ExpressionBuilder(new SpreadsheetFactory());
            b.buildExpression(exprText);

            Expression expr = (Expression) b.getExpression();
            return evaluate(expr, sheet, visiting);
        }

        throw new IllegalArgumentException("Referenced cell is not numeric: " + content.getRaw());
    }
}
