package spreadsheet;

import parser.ExpressionBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;


// Spreadsheet stores cells and their raw contents (Task 1–2)
public class Spreadsheet {

    private final Map<Coordinate, Cell> cells = new HashMap<>();
    private final DependencyTracker tracker = new DependencyTracker();

    public void setCell(Coordinate coordinate, Content content) {

        // create or update existing cell
        Cell cell = cells.get(coordinate);
        if (cell == null) {
            cell = new Cell(coordinate, content);
            cells.put(coordinate, cell);
        } else {
            cell.setContent(content);
        }

        // remove old dependencies for this cell (if any)
        tracker.clearDependenciesOf(coordinate);

        // parse formula and collect dependencies (if applicable)
        if (content instanceof FormulaContent) {
            String raw = content.getRaw();
            String exprText = raw.startsWith("=") ? raw.substring(1) : raw;

            try {
                ExpressionBuilder builder = new ExpressionBuilder(new SpreadsheetFactory());
                builder.buildExpression(exprText);

                Expression expr = (Expression) builder.getExpression();
                tracker.setDependencies(coordinate, collectDependencies(expr));

            } catch (Exception ex) {
                // Do not crash the application.
                // Formula errors will be reported later
            }
        }

        // invalidate this cell and all dependent cells
        invalidateFrom(coordinate);
    }

    public List<Coordinate> setCellAndGetAffected(Coordinate changed, Content content) {
        setCell(changed, content); // your existing logic (updates deps + invalidateFrom)


        return tracker.getTransitiveDependents(changed);
    }



    public Content getCellContent(Coordinate coordinate) {
        Cell cell = cells.get(coordinate);
        return (cell != null) ? cell.getContent() : null;
    }

    public Map<Coordinate, Cell> getCells() {
        return cells;
    }

    public String getDisplayValue(Coordinate coordinate) {

        Cell cell = cells.get(coordinate);
        if (cell == null) {
            return "<empty>";
        }

        Content content = cell.getContent();

        // Non-formula → show raw, no caching needed
        if (!(content instanceof FormulaContent)) {
            return content.getRaw();
        }

        String raw = content.getRaw();
        String exprText = raw.startsWith("=") ? raw.substring(1) : raw;

        try {
            // Parse only once per content change
            if (cell.getCachedExpr() == null) {
                ExpressionBuilder builder = new ExpressionBuilder(new SpreadsheetFactory());
                builder.buildExpression(exprText);
                Expression expr = (Expression) builder.getExpression();
                cell.setCachedExpr(expr);
            }

            // Recompute only if dirty or not cached
            if (cell.isDirty() || cell.getCachedValue() == null) {
                Evaluator evaluator = new Evaluator();
                double value = evaluator.evaluate(cell.getCachedExpr(), this);

                cell.setCachedValue(value);
                cell.setDirty(false);
            }

            return raw + " -> " + cell.getCachedValue();

        } catch (Exception e) {
            return raw + " -> ERROR: " +
                    (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
        }
    }

    private Set<Coordinate> collectDependencies(Expression expr) {
        Set<Coordinate> deps = new HashSet<>();
        walkDependencies(expr, deps);
        return deps;
    }

    private void walkDependencies(Expression e, Set<Coordinate> out) {

        if (e instanceof Literal) {
            return;
        }

        if (e instanceof CellRef c) {
            out.add(c.toCoordinate());
            return;
        }

        if (e instanceof RangeRef r) {
            out.addAll(r.expand());
            return;
        }

        if (e instanceof BinaryOp b) {
            walkDependencies(b.getLeft(), out);
            walkDependencies(b.getRight(), out);
            return;
        }

        if (e instanceof FunctionCall f) {
            for (Expression arg : f.getArgs()) {
                walkDependencies(arg, out);
            }
            return;
        }

        if (e instanceof ErrorExpression) {
            return;
        }

        throw new IllegalArgumentException("Unknown expression type in dependency collection: " + e.getClass().getName());
    }

    private void invalidateFrom(Coordinate start) {
        java.util.ArrayDeque<Coordinate> q = new java.util.ArrayDeque<>();
        java.util.HashSet<Coordinate> seen = new java.util.HashSet<>();

        q.add(start);
        seen.add(start);

        while (!q.isEmpty()) {
            Coordinate cur = q.removeFirst();

            Cell c = cells.get(cur);
            if (c != null) {
                c.setDirty(true);
                c.setCachedValue(null);
            }

            for (Coordinate dep : tracker.getDependents(cur)) {
                if (seen.add(dep)) {
                    q.add(dep);
                }
            }
        }
    }

}
