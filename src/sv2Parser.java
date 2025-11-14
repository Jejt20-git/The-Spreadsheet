import java.io.*;
import java.util.*;

public class sv2Parser {
    public static Spreadsheet fromFile(String path) throws IOException {
        Spreadsheet sheet = new Spreadsheet();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.strip();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|", 3);
                if (parts.length < 3) continue;
                Coordinate c = Coordinate.fromA1(parts[0]);
                String kind = parts[1];
                String raw = parts[2];

                Content content;
                switch (kind) {
                    case "NUM":  content = new NumericContent(Double.valueOf(raw)); break;
                    case "FORM": content = new FormulaContent(raw); break;
                    default:     content = new TextContent(raw);
                }
                sheet.setContent(c, content);
            }
        }
        return sheet;
    }
}

