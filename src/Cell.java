public class Cell {
    private final Coordinate coord;
    private Content content; // never null

    public Cell(Coordinate coord) {
        this.coord = coord;
        this.content = new TextContent(""); // empty by default
    }

    public Coordinate getCoord() { return coord; }
    public Content getContent()  { return content; }
    public void setContent(Content content) {
        if (content == null) content = new TextContent("");
        this.content = content;
    }
}
