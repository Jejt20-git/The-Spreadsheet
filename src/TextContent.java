public class TextContent extends Content {
    public final String text;
    public TextContent(String text) { this.text = text == null ? "" : text; }
    @Override public String raw() { return text; }
    @Override public String toString() { return text; }
}

