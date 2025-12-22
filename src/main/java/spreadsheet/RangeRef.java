package spreadsheet;

import java.util.ArrayList;
import java.util.List;

public class RangeRef implements Expression {
    private final Coordinate start;
    private final Coordinate end;

    public RangeRef(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
    }

    public Coordinate getStart() { return start; }
    public Coordinate getEnd() { return end; }

    public List<Coordinate> expand() {
        List<Coordinate> coords = new ArrayList<>();

        int rowStart = Math.min(start.getRowIndex(), end.getRowIndex());
        int rowEnd   = Math.max(start.getRowIndex(), end.getRowIndex());
        int colStart = Math.min(start.getColIndex(), end.getColIndex());
        int colEnd   = Math.max(start.getColIndex(), end.getColIndex());

        for (int r = rowStart; r <= rowEnd; r++) {
            for (int c = colStart; c <= colEnd; c++) {
                coords.add(new Coordinate(r, c));
            }
        }

        return coords;
    }
}
