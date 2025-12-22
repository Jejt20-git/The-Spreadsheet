package ui;

import spreadsheet.Coordinate;

/**
 * UI helper: converts user-friendly refs like "A1" / "AA10" to Coordinate(row, col).
 * Keeps Coordinate clean (no UI string parsing inside the domain model).
 */
public final class RefParser {

    private RefParser() {
        // utility class
    }

    public static Coordinate parseRef(String ref) {
        ref = ref.trim().toUpperCase();

        if (!ref.matches("[A-Z]+[0-9]+")) {
            throw new IllegalArgumentException("Invalid cell reference. Use e.g. A1, B2, AA10");
        }

        int i = 0;
        while (i < ref.length() && Character.isLetter(ref.charAt(i))) i++;

        String colPart = ref.substring(0, i);
        String rowPart = ref.substring(i);

        int row = Integer.parseInt(rowPart);

        int col = 0;
        for (char ch : colPart.toCharArray()) {
            col = col * 26 + (ch - 'A' + 1);
        }

        return new Coordinate(row, col);
    }
    public static String toRef(Coordinate c) {
        int col = c.getColIndex();
        int row = c.getRowIndex();

        StringBuilder sb = new StringBuilder();
        while (col > 0) {
            int rem = (col - 1) % 26;
            sb.append((char) ('A' + rem));
            col = (col - 1) / 26;
        }

        return sb.reverse().toString() + row;
    }

}
