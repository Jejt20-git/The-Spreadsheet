package spreadsheet.IO;

import spreadsheet.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class sv2Parser {

    public void loadFromFile(String filename, Spreadsheet spreadsheet) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String line;
            while ((line = br.readLine()) != null) {

                if (!line.contains("=")) continue;

                String[] parts = line.split("=", 2);
                String coordinateStr = parts[0].trim();
                String rawContent = parts[1].trim();

                Coordinate coordinate = parseCoordinate(coordinateStr);

                Content content;
                if (rawContent.startsWith("=")) {
                    content = new FormulaContent(rawContent);      // store formula raw
                } else if (isNumber(rawContent)) {
                    content = new NumericContent(rawContent);      // store number raw
                } else {
                    content = new TextContent(rawContent);         // store text raw
                }

                spreadsheet.setCell(coordinate, content);
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

    private Coordinate parseCoordinate(String coordinateStr) {
        char colChar = coordinateStr.charAt(0);
        int col = colChar - 'A' + 1;
        int row = Integer.parseInt(coordinateStr.substring(1));
        return new Coordinate(row, col);
    }
}
