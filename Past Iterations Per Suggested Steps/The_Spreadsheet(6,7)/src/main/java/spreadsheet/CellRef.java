package spreadsheet;

public class CellRef implements Expression {
    private final String ref;  // e.g. "A1"

    public CellRef(String ref) { this.ref = ref; }

    public String ref() { return ref; }

    // Convert "A1" to Coordinate(row=1, col=1)
    public Coordinate toCoordinate() {
        if (ref == null) throw new IllegalArgumentException("Null cell reference");

        String s = ref.trim().toUpperCase();
        if (s.isEmpty()) throw new IllegalArgumentException("Empty cell reference");

        int i = 0;
        int col = 0;

        while (i < s.length() && Character.isLetter(s.charAt(i))) {
            col = col * 26 + (s.charAt(i) - 'A' + 1);
            i++;
        }
        if (col == 0) throw new IllegalArgumentException("Invalid cell reference: " + ref);

        int row;
        try {
            row = Integer.parseInt(s.substring(i));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid cell reference: " + ref);
        }
        if (row <= 0) throw new IllegalArgumentException("Invalid cell reference: " + ref);

        // coordinate constructor is (rowIndex, colIndex)
        return new Coordinate(row, col);
    }
}
