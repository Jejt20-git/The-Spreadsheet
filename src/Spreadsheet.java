import java.util.*;

public class Spreadsheet {
    private final Map<Coordinate, Cell> cells = new HashMap<>();

    public Map<Coordinate, Cell> cells() { return Collections.unmodifiableMap(cells); }

    private Cell ensureCell(Coordinate c) { return cells.computeIfAbsent(c, Cell::new); }

    // Set content by object (Text/Numeric/Formula).
    public void setContent(Coordinate c, Content content) {
        ensureCell(c).setContent(content);
    }

    // Convenience: infer type from raw user input.
    public void setContent(Coordinate c, String raw) {
        setContent(c, Parser.parseCellContent(raw));
    }

    public Content getContent(Coordinate c) {
        Cell cell = cells.get(c);
        return (cell == null) ? new TextContent("") : cell.getContent();
    }

    // For UI/log: show the content exactly as no evaluation
    public String showCell(Coordinate c) { return getContent(c).toString(); }
}
