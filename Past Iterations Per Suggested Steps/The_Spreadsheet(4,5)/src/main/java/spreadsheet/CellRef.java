package spreadsheet;

public class CellRef implements Expression {
    private final String ref;  // e.g. "A1"

    public CellRef(String ref) { this.ref = ref; }

    public String ref() { return ref; }
}
