package spreadsheet;

import parser.AbstractFactory;
import parser.AbstractNode;

public class SpreadsheetFactory implements AbstractFactory {

    @Override
    public AbstractNode buildOperator(char c, AbstractNode left, AbstractNode right) {
        Expression l = (Expression) left;
        Expression r = (Expression) right;

        BinaryOp.Operator op = map(c);
        return new BinaryOp(op, l, r);
    }

    @Override
    public AbstractNode buildFunction(String name, AbstractNode[] args) {

        String upper = name.toUpperCase();



        // Step 8: only MIN/MAX
        if (!upper.equals("MAX") && !upper.equals("MIN")) {
            return new ErrorExpression("Unknown function: " + name);
        }

        java.util.List<Expression> exprArgs = new java.util.ArrayList<>();
        for (AbstractNode a : args) {
            exprArgs.add((Expression) a); // our Expression extends AbstractNode
        }

        return new FunctionCall(upper, exprArgs);
    }


    @Override
    public AbstractNode buildNumberConstant(double v) {
        return new Literal(v);
    }

    @Override
    public AbstractNode buildTextConstant(String s) {
        // If the parser supports quoted strings, but your evaluator is numeric-only for now
        return new ErrorExpression("Text constants not supported in numeric formulas: " + s);
    }

    @Override
    public AbstractNode buildCellReference(String s) {
        // Stage after this will actually resolve A1 -> spreadsheet value
        return new CellRef(s);
    }

    @Override
    public AbstractNode buildError(String s) {
        return new ErrorExpression("Parse error: " + s);
    }

    private BinaryOp.Operator map(char op) {
        return switch (op) {
            case '+' -> BinaryOp.Operator.ADD;
            case '-' -> BinaryOp.Operator.SUB;
            case '*' -> BinaryOp.Operator.MUL;
            case '/' -> BinaryOp.Operator.DIV;
            default -> throw new IllegalArgumentException("Unknown operator: " + op);
        };
    }
}
