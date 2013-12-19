package structural.postprocess;

import jeslime.mock.MockGeometry;
import junit.framework.TestCase;
import no.uib.cipr.matrix.DenseVector;
import structural.EpsilonUtil;
import structural.Flags;
import structural.identifiers.Coordinate;

/**
 * Created by dbborens on 12/16/13.
 */
public class SolutionViewerTest extends TestCase {
    private MockGeometry geom;
    private SolutionViewer viewer;
    private Coordinate origin;
    @Override
    protected void setUp() throws Exception {
        geom = new MockGeometry();

        origin = new Coordinate(0, 0, Flags.VECTOR);
        geom.setCanonicalSites(new Coordinate[] {origin});
        geom.setCenter(origin);
        DenseVector solution = new DenseVector(1);
        solution.set(0, 1.0);

        viewer = new SolutionViewer(solution, geom);
    }

    public void testGet() throws Exception {
        assertEquals(1.0, viewer.get(origin), EpsilonUtil.epsilon());
    }

    public void testGetSolution() throws Exception {
        DenseVector solution = viewer.getSolution();

        assertEquals(1, solution.size());
        assertEquals(1.0, solution.get(0), EpsilonUtil.epsilon());
    }
}
