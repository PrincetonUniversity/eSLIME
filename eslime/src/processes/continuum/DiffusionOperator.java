/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.continuum;

import control.identifiers.Coordinate;
import geometry.Geometry;
import no.uib.cipr.matrix.DenseMatrix;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Created by dbborens on 1/24/15.
 */
public class DiffusionOperator extends DenseMatrix {

    public DiffusionOperator(DiffusionConstantHelper helper, Geometry geometry) {
        this(geometry.getCanonicalSites().length);
        load(helper, geometry);
    }

    private void load(DiffusionConstantHelper helper, Geometry geometry) {
        Coordinate[] sites = geometry.getCanonicalSites();
        int n = sites.length;
        Function<Coordinate, Integer> indexer = geometry.getIndexer();
        IntStream.range(0, n)
                .boxed()
                .forEach(j -> {
                    Coordinate coord = sites[j];

                    // Set the diagonal value
                    add(j, j, helper.getDiagonalValue());

                    // Set each neighbor. For reflecting boundary conditions, one or
                    // more neighbors may be the diagonal.
                    Arrays.asList(geometry.getNeighbors(coord, Geometry.APPLY_BOUNDARIES))
                            .stream()
                            .map(indexer)
                            .forEach(i -> add(i, j, helper.getNeighborValue()));

                });
    }

    private DiffusionOperator(int n) {
        super(n, n);

    }
}
