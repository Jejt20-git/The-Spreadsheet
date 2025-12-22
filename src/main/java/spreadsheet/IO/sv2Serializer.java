package spreadsheet.IO;

import spreadsheet.Cell;
import spreadsheet.Content;
import spreadsheet.Coordinate;
import spreadsheet.Spreadsheet;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class sv2Serializer {

    public void saveToFile(String filename, Spreadsheet spreadsheet) throws IOException {
        Map<Coordinate, Cell> cells = spreadsheet.getCells();

        // If no cells exist, write empty file.
        if (cells == null || cells.isEmpty()) {
            try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
                // empty
            }
            return;
        }

        // Find bounds (max row/col used)
        int maxRow = 0;
        int maxCol = 0;
        for (Coordinate c : cells.keySet()) {
            if (c.getRowIndex() > maxRow) maxRow = c.getRowIndex();
            if (c.getColIndex() > maxCol) maxCol = c.getColIndex();
        }

        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {

            for (int r = 1; r <= maxRow; r++) {

                // Track last non-empty column so we can trim trailing empties
                int lastNonEmptyCol = 0;

                // First pass: determine last non-empty col in this row
                for (int c = 1; c <= maxCol; c++) {
                    Cell cell = cells.get(new Coordinate(r, c));
                    if (cell != null && cell.getContent() != null) {
                        String raw = cell.getContent().getRaw();
                        if (raw != null && !raw.trim().isEmpty()) {
                            lastNonEmptyCol = c;
                        }
                    }
                }

                // Entire row empty -> write empty line
                if (lastNonEmptyCol == 0) {
                    out.println();
                    continue;
                }

                // Build row up to lastNonEmptyCol
                StringBuilder line = new StringBuilder();
                for (int c = 1; c <= lastNonEmptyCol; c++) {
                    if (c > 1) line.append(';');

                    Cell cell = cells.get(new Coordinate(r, c));
                    if (cell == null || cell.getContent() == null) {
                        // empty token => blank cell
                        continue;
                    }

                    String token = cell.getContent().getRaw();
                    if (token == null) token = "";
                    token = token.trim();

                    // In the file format: formulas must store function args using ',' not ';'
                    if (token.startsWith("=")) {
                        token = token.replace(';', ',');
                    }

                    line.append(token);
                }

                out.println(line);
            }
        }
    }
}
