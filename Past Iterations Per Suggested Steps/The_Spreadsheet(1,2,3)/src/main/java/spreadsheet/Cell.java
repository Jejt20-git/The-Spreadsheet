package spreadsheet;

// At this point the cell will just consist of a coordinate and content
// Deliverarble 3 will implement evaluation of formulas.
public class Cell {
    private final Coordinate coordinate;
    private Content  content;

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
    }
}
