/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package test;

import control.identifiers.Coordinate;
import geometry.Geometry;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;
import org.junit.Before;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dbborens on 1/9/15.
 */
public abstract class LinearMocks extends TestBase {

    protected Geometry geom;
    protected Coordinate[] cc;
    protected Coordinate a, b, c;
    protected Function<Coordinate, Integer> indexer;

    protected static Coordinate[] makeCanonicalCoordinates() {
        Coordinate[] cc = new Coordinate[3];
        cc = IntStream.range(0, 3)
                .boxed()
                .map(x -> new Coordinate(0, x, 0))
                .collect(Collectors.toList()).toArray(cc);

        return cc;
    }

    protected static DenseVector vector(double x0, double x1, double x2) {
        double[] arr = new double[]{x0, x1, x2};
        return new DenseVector(arr);
    }

    protected static DenseMatrix matrix(double x00, double x11, double x22) {
        DenseMatrix matrix = new DenseMatrix(3, 3);
        matrix.add(0, 0, x00);
        matrix.add(1, 1, x11);
        matrix.add(2, 2, x22);
        return matrix;
    }

    @Before
    public void makeGeometry() {
        cc = makeCanonicalCoordinates();

        geom = mock(Geometry.class);
        when(geom.getCanonicalSites()).thenReturn(cc);

        a = cc[0];
        b = cc[1];
        c = cc[2];

        indexer = c -> c.y();
    }

    protected void checkMatrix(Matrix actual, double x0, double x1, double x2) {
        Matrix expected = matrix(x0, x1, x2);
        assertMatricesEqual(expected, actual, epsilon);
    }

    protected void checkVector(Vector actual, double x0, double x1, double x2) {
        DenseVector expected = vector(x0, x1, x2);
        assertVectorsEqual(expected, actual, epsilon);
    }
}
