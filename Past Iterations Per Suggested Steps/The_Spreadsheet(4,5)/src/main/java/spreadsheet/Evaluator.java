package spreadsheet;

public class Evaluator {

    public double evaluate(Expression e) {

        if (e instanceof Literal lit) {
            return lit.value();
        }

        if (e instanceof BinaryOp b) {
            double left  = evaluate(b.getLeft());
            double right = evaluate(b.getRight());

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
            throw new UnsupportedOperationException(
                    "Cell references not implemented yet: " + c.ref()
            );
        }


        throw new IllegalArgumentException("Unknown expression type: " + e.getClass().getName());
    }
}
