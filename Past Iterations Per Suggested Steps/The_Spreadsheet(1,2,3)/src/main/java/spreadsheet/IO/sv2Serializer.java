package spreadsheet.IO;

import spreadsheet.Cell;
import spreadsheet.Content;
import spreadsheet.Coordinate;
import spreadsheet.Spreadsheet;

import java.io.*;
import java.util.*;

public class sv2Serializer {
    public void saveToFile(String filename, Spreadsheet spreadsheet) throws IOException {

        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            for (Map.Entry<Coordinate, Cell> entry : spreadsheet.getCells().entrySet()) {

                Coordinate coordinate = entry.getKey();
                Cell cell = entry.getValue();
                Content content = cell.getContent();

                String ref = coordinateToA1(coordinate);

                out.println(ref + "=" + content.toString());
            }

        }
    }

    private String coordinateToA1(Coordinate c) {
        char col = (char) ('A' + c.getColIndex() - 1);
        return col + Integer.toString(c.getRowIndex());
    }
}
