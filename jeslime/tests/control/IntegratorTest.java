package control;

import geometry.MockGeometry;
import geometry.MockGeometryManager;
import io.project.MockProcessLoader;
import io.serialize.MockSerializationManager;
import junit.framework.TestCase;
import layers.LayerManager;
import layers.MockLayerManager;
import layers.MockSoluteLayer;
import layers.cell.CellLayer;
import structural.MockGeneralParameters;
import structural.identifiers.Coordinate;

/**
 * Created by David B Borenstein on 1/7/14.
 */
public class IntegratorTest extends TestCase {

    /**
     * This class is the core of the simulation, so it has a lot of
     * dependencies. Still, this many mocks suggests that a refactor
     * is in order. This is particularly evident given that this
     * class is so hard to test.
     */

    // Items used during construction
    private MockGeneralParameters mgp;
    private MockProcessLoader pl;
    private MockGeometry geom;
    private MockGeometryManager gm;
    private MockSerializationManager sm;
    private CellLayer cl;
    private MockSoluteLayer sl;
    private MockLayerManager lm;

    @Override
    protected void setUp() throws Exception {
        // Initialize infrastructure objects
        mgp = new MockGeneralParameters();
        pl = new MockProcessLoader();
        geom = new MockGeometry();
        gm = new MockGeometryManager();
        sm = new MockSerializationManager();
        lm = new MockLayerManager();

        // Initialize layers
        cl = new CellLayer(geom, 0);
        sl = new MockSoluteLayer();

        // Point everybody at each other
        Coordinate[] cc = new Coordinate[] {
                new Coordinate(0, 0, 0)
        };

        geom.setCanonicalSites(cc);

        lm.addSoluteLayer("solute", sl);
        lm.setCellLayer(cl);
        gm.setGeometry(geom);
    }

    public void testGo() throws Exception {
        // Set T to 5 loops.

        // Each of the following should have been called 5 times:

        // processManager::getTriggeredProcesses()
            // MockProcessManager::gtp should iterate an internal
            // counter and return an empty list


        fail("Not yet implemented");
    }

    public void testPostprocess() throws Exception {
        fail("Not yet implemented");
    }

}
