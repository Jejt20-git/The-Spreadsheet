import java.util.Objects;

public class Coordinate {
    public final int colIndex; // 0-based
    public final int row;      // 1-based

    public Coordinate(int colIndex, int row) {
        if (colIndex < 0 || row < 1) throw new IllegalArgumentException("Bad coord");
        this.colIndex = colIndex;
        this.row = row;
    }

    // "A1" -> new Coordinate(0,1)
    public static Coordinate fromA1(String a1) {
        a1 = a1.trim().toUpperCase();
        int i = 0;
        int col = 0;
        while (i < a1.length() && Character.isLetter(a1.charAt(i))) {
            col = col * 26 + (a1.charAt(i) - 'A' + 1);
            i++;
        }
        int row = Integer.parseInt(a1.substring(i));
        return new Coordinate(col - 1, row);
    }

    // 0 -> A, 27 -> AB, etc.
    private static String colToLetters(int colIndex) {
        int x = colIndex + 1;
        StringBuilder sb = new StringBuilder();
        while (x > 0) {
            int rem = (x - 1) % 26;
            sb.append((char)('A' + rem));
            x = (x - 1) / 26;
        }
        return sb.reverse().toString();
    }

    @Override public String toString() { return colToLetters(colIndex) + row; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;
        Coordinate that = (Coordinate) o;
        return colIndex == that.colIndex && row == that.row;
    }
    @Override public int hashCode() { return Objects.hash(colIndex, row); }
}
