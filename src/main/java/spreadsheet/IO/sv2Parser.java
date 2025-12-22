package spreadsheet.IO;

import spreadsheet.Content;
import spreadsheet.Coordinate;
import spreadsheet.FormulaContent;
import spreadsheet.NumericContent;
import spreadsheet.Spreadsheet;
import spreadsheet.TextContent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class sv2Parser {

    public void loadFromFile(String filename, Spreadsheet spreadsheet) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String line;
            int row = 1;

            while ((line = br.readLine()) != null) {

                // Split by ';' and KEEP empty tokens (important for ";;")
                String[] tokens = line.split(";", -1);

                for (int col = 1; col <= tokens.length; col++) {
                    String raw = tokens[col - 1];

                    // Empty token => blank cell (do nothing)
                    if (raw == null) continue;
                    raw = raw.trim();
                    if (raw.isEmpty()) continue;

                    Content content;

                    if (raw.startsWith("=")) {
                        // Convert stored ',' back to ';' for function argument separator in formulas
                        String formula = raw.replace(',', ';');
                        content = new FormulaContent(formula);

                    } else if (isNumber(raw)) {
                        content = new NumericContent(raw);

                    } else {
                        content = new TextContent(raw);
                    }

                    spreadsheet.setCell(new Coordinate(row, col), content);
                }

                row++;
            }
        }
    }

    private boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
