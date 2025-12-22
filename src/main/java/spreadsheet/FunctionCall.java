package spreadsheet;

import java.util.List;

public class FunctionCall implements Expression {
    private final String name;
    private final List<Expression> args;

    public FunctionCall(String name, List<Expression> args) {
        this.name = name.toUpperCase();
        this.args = List.copyOf(args);
    }

    public String getName() { return name; }
    public List<Expression> getArgs() { return args; }
}
