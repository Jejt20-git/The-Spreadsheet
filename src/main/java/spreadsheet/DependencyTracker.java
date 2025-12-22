package spreadsheet;

import java.util.*;

public class DependencyTracker {
    private final Map<Coordinate, Set<Coordinate>> depsOf = new HashMap<>();
    private final Map<Coordinate, Set<Coordinate>> dependentsOf = new HashMap<>();

    public void clearDependenciesOf(Coordinate cell) {
        Set<Coordinate> oldDeps = depsOf.remove(cell);
        if (oldDeps == null) return;

        for (Coordinate dep : oldDeps) {
            Set<Coordinate> dependents = dependentsOf.get(dep);
            if (dependents != null) {
                dependents.remove(cell);
                if (dependents.isEmpty()) dependentsOf.remove(dep);
            }
        }
    }

    public void setDependencies(Coordinate cell, Set<Coordinate> newDeps) {
        clearDependenciesOf(cell);
        depsOf.put(cell, new HashSet<>(newDeps));

        for (Coordinate dep : newDeps) {
            dependentsOf.computeIfAbsent(dep, k -> new HashSet<>()).add(cell);
        }
    }

    public Set<Coordinate> getDependents(Coordinate cell) {
        return dependentsOf.getOrDefault(cell, Collections.emptySet());
    }
    public List<Coordinate> getTransitiveDependents(Coordinate start) {
        List<Coordinate> result = new ArrayList<>();
        Set<Coordinate> visited = new HashSet<>();
        Deque<Coordinate> stack = new ArrayDeque<>();

        stack.push(start);
        visited.add(start);

        while (!stack.isEmpty()) {
            Coordinate cur = stack.pop();

            for (Coordinate dep : getDependents(cur)) {
                if (visited.add(dep)) {
                    result.add(dep);     // dep depends on start (directly or indirectly)
                    stack.push(dep);     // explore further
                }
            }
        }

        return result;
    }
}
