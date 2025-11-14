public class Parser {
    /** Minimal inference for D2: "=..." -> Formula, numeric -> Numeric, else Text. */
    public static Content parseCellContent(String raw) {
        if (raw == null) return new TextContent("");
        raw = raw.strip();
        if (raw.startsWith("=")) return new FormulaContent(raw);
        try {
            if (!raw.isEmpty()) return new NumericContent(Double.valueOf(raw));
        } catch (NumberFormatException ignore) { /* fall through to text */ }
        return new TextContent(raw);
    }

    // Placeholder for D3:
    //public static Expr parseFormula(String raw) { return null; }
}
