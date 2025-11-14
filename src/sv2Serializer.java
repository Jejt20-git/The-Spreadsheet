import java.io.*;
import java.util.Map;

public class sv2Serializer {
    public static void toFile(Spreadsheet sheet, String path) throws IOException {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path)))) {
            pw.println("# SV2 v1");
            for (Map.Entry<Coordinate, Cell> e : sheet.cells().entrySet()) {
                Coordinate c = e.getKey();
                Content k = e.getValue().getContent();
                String kind = (k instanceof NumericContent) ? "NUM"
                        : (k instanceof FormulaContent) ? "FORM" : "TEXT";
                pw.println(c + "|" + kind + "|" + k.raw());
            }
        }
    }
}
