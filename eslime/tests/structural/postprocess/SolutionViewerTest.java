/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package structural.postprocess;

import control.identifiers.Coordinate;
import control.identifiers.Flags;
import geometry.MockGeometry;
import junit.framework.TestCase;
import no.uib.cipr.matrix.DenseVector;
import structural.utilities.EpsilonUtil;

/**
 * Created by dbborens on 12/16/13.
 */
public class SolutionViewerTest extends TestCase {
    private MockGeometry geom;
    private SolutionViewer viewer;
    private Coordinate origin;
    private Coordinate other;

    @Override
    protected void setUp() throws Exception {
        geom = new MockGeometry();

        origin = new Coordinate(1, 1, Flags.VECTOR);
        other = new Coordinate(1, 2, Flags.VECTOR);

        Coordinate[] cc = new Coordinate[]{
                origin.canonicalize(),
                other.canonicalize()
        };

        geom.setCanonicalSites(cc);

        // Make sure that got assigned the way we expect
        assertEquals(0, geom.coordToIndex(origin).intValue());
        assertEquals(1, geom.coordToIndex(other).intValue());

        geom.setCenter(origin.canonicalize());

        DenseVector solution = new DenseVector(2);
        solution.set(0, 1.0);
        solution.set(1, 5.0);

        viewer = new SolutionViewer(solution, geom);
    }

    public void testGet() throws Exception {
        Coordinate originOffset = new Coordinate(0, 0, Flags.VECTOR);
        Coordinate otherOffset = new Coordinate(0, 1, Flags.VECTOR);

        assertEquals(1.0, viewer.getRelative(originOffset), EpsilonUtil.epsilon());
        assertEquals(5.0, viewer.getRelative(otherOffset), EpsilonUtil.epsilon());
    }

    public void testGetSolution() throws Exception {
        DenseVector solution = viewer.getSolution();

        assertEquals(2, solution.size());
        assertEquals(1.0, solution.get(0), EpsilonUtil.epsilon());
        assertEquals(5.0, solution.get(1), EpsilonUtil.epsilon());
    }

    public void testGetAbsolute() throws Exception {
        assertEquals(1.0, viewer.getAbsolute(origin.canonicalize()), EpsilonUtil.epsilon());
        assertEquals(5.0, viewer.getAbsolute(other.canonicalize()), EpsilonUtil.epsilon());
    }
}
