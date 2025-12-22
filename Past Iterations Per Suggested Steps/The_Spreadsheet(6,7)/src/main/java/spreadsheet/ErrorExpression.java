package spreadsheet;

public class ErrorExpression implements Expression {
    private final String message;

    public ErrorExpression(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
