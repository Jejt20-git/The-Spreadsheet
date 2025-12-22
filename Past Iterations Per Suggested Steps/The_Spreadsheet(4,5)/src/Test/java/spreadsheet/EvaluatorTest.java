package spreadsheet;


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
}