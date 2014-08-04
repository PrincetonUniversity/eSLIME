package processes.discrete.filter;

import control.arguments.Argument;
import control.identifiers.Coordinate;
import layers.cell.CellLayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Removes all individuals at greater than a certain depth from the surface
 * of the population (i.e., the nearest vacant site).
 *
 * Created by dbborens on 8/1/14.
 */
public class DepthFilter extends Filter {

    private Argument<Integer> maxDepth;
    private CellLayer layer;

    public DepthFilter(CellLayer layer, Argument<Integer> maxDepth) {
        this.maxDepth = maxDepth;
        this.layer = layer;
    }
    @Override
    public List<Coordinate> apply(List<Coordinate> toFilter) {
        // Get current depth value.
        int depth = maxDepth.next();

        // We have to use a set because, with some boundary conditions, we could
        // see the same site twice.
        HashSet<Coordinate> set = new HashSet<>(toFilter.size());

        // Consider each set in the unfiltered pool.
        for (Coordinate c : toFilter) {

            // Find the nearest vacancies within a radius up to and including
            // the maximum permissible. If a cell has vacant neighbors, it is at
            // the surface (depth = 0), and if it is next to cells with vacant
            // neigbhors, it is at depth 1. But the argument for getNearestVacancies
            // is an exclusive boundary, so add 1 to the argument.
            Coordinate[] vacancies = layer.getLookupManager().getNearestVacancies(c, depth + 1);

            // If any vacancies were found within the permissible depth, then
            // the coordinate meets requirements.
            if (vacancies.length > 0) {
                set.add(c);
            }
        }

        List<Coordinate> ret = new ArrayList<>(set);
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DepthFilter)) return false;

        DepthFilter that = (DepthFilter) o;

        if (!maxDepth.equals(that.maxDepth)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return maxDepth.hashCode();
    }
}
