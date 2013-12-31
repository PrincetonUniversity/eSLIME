package layers;

import io.project.GeometryManager;
import layers.cell.CellLayer;
import layers.solute.SoluteLayer;
import org.dom4j.Element;

import java.util.HashMap;

/**
 * Created by dbborens on 12/31/13.
 */
public class MockLayerManager extends LayerManager {
    public MockLayerManager() {
        super();
        soluteLayers = new HashMap<>();
    }

    public CellLayer getCellLayer() {
        return cellLayer;
    }

    public void setCellLayer(CellLayer cellLayer) {
        this.cellLayer = cellLayer;
    }

    private CellLayer cellLayer;

    private HashMap<String, SoluteLayer> soluteLayers;


    public void addSoluteLayer(String s, SoluteLayer layer) {
        soluteLayers.put(s, layer);
    }
}
