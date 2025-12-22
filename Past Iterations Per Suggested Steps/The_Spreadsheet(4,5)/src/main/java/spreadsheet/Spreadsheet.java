package spreadsheet;

import java.util.HashMap;
import java.util.Map;


public class Spreadsheet {

    private final Map<Coordinate, Cell> cells = new HashMap<>();

    public void setCell(Coordinate coordinate, Content content) {
        cells.put(coordinate, new Cell(coordinate, content));
    }

    public Content getCellContent(Coordinate coordinate) {
        Cell cell = cells.get(coordinate);
        return (cell != null) ? cell.getContent() : null;
    }

    public Map<Coordinate, Cell> getCells() {
        return cells;
    }
}
