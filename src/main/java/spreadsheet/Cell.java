package spreadsheet;

public class Cell {
    private final Coordinate coordinate;
    private Content content;

    // Step 9: caching + invalidation
    private boolean dirty = true;
    private Double cachedValue = null;
    private Expression cachedExpr = null;

    public Cell(Coordinate coordinate, Content content) {
        this.coordinate = coordinate;
        this.content = content;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;

        // whenever content changes, cached results are no longer valid
        this.dirty = true;
        this.cachedValue = null;
        this.cachedExpr = null;
    }

    // ---- dirty flag ----
    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    // ---- cached numeric value ----
    public Double getCachedValue() {
        return cachedValue;
    }

    public void setCachedValue(Double cachedValue) {
        this.cachedValue = cachedValue;
    }

    // ---- cached parsed expression (AST) ----
    public Expression getCachedExpr() {
        return cachedExpr;
    }

    public void setCachedExpr(Expression cachedExpr) {
        this.cachedExpr = cachedExpr;
    }
}
