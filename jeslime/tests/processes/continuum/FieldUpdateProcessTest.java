package processes.continuum;

import geometry.Geometry;
import io.project.ProcessLoader;
import junit.framework.TestCase;
import layers.LayerManager;
import no.uib.cipr.matrix.DenseVector;
import processes.gillespie.GillespieState;
import structural.halt.HaltCondition;
import sun.net.www.content.audio.wav;

/**
 * Created by David B Borenstein on 1/7/14.
 */
public class FieldUpdateProcessTest extends TestCase {

    public void testTargetSimple() throws Exception {
        fail("Not yet implemented");
    }

    public void testTargetGillespie() throws Exception {
        fail("Not yet implemented");
    }

    public void testLifeCycle() throws Exception {
        fail("Not yet implemented");
    }

    public void testCheckForProduction() throws Exception {
        fail("Not yet implemented");
    }
    private class ExposedFieldUpdateProcess extends FieldUpdateProcess {
        public ExposedFieldUpdateProcess(ProcessLoader loader, int processId, LayerManager layerManager, String layerId) {
            super(loader, processId, layerManager, layerId);
        }

        @Override
        public DenseVector checkForProduction() {
            return super.checkForProduction();
        }

    }
}
