package spreadsheet;

public abstract class Content {
    // Just being used as a common parent type for now
    protected final String raw;
    public Content(String raw) {
        this.raw = raw;
    }
    public String getRaw() {
        return raw;
    }
    public String toString() {
        return raw;
    }
}
