public abstract class Content {
    public boolean isEmpty() { return false; }
    /** For saving/showing raw user-entered content (e.g., "=SUM(A1:B2)" or "hello"). */
    public abstract String raw();
    /** Human-friendly display of the content (no evaluation yet). */
    @Override public abstract String toString();
}
