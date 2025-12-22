package spreadsheet;

public class BinaryOp implements Expression {

    public enum Operator {
        ADD, SUB, MUL, DIV;
    }

    private final Operator op;
    private final Expression left;
    private final Expression right;

    public BinaryOp(Operator op, Expression left, Expression right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }
    public Operator getOp() { return op; }
    public Expression getLeft() { return left; }
    public Expression getRight() { return right; }
}
