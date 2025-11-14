public class FormulaContent extends Content {
    public final String raw; // keep full "=..." for now
    //public Expr expr;        // placeholder for D3
    public FormulaContent(String raw) {
        if (raw == null || !raw.startsWith("="))
            throw new IllegalArgumentException("Formula must start with =");
        this.raw = raw;
    }
    @Override public String raw() { return raw; }
    @Override public String toString() { return raw; }
}
