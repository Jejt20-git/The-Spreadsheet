package spreadsheet;

public class Coordinate {
    private final int rowIndex;
    private final int colIndex;


    // Constructor
    public Coordinate(int rowIndex, int colIndex) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    // Getters
    public int getRowIndex() {
        return rowIndex;
    }
    public int getColIndex() {
        return colIndex;
    }

    public int hashCode() {
        return 31 * rowIndex + colIndex;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Coordinate)) return false;

        Coordinate other = (Coordinate) obj;
        return this.colIndex == other.colIndex && this.rowIndex == other.rowIndex;
    }


}
