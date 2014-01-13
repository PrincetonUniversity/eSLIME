package geometry;

import io.project.GeometryManager;
import org.dom4j.Element;

/**
 * Created by David B Borenstein on 1/11/14.
 */
public class MockGeometryManager extends GeometryManager {

    private Geometry geometry;

    public MockGeometryManager() {
        super();
    }

    /**
     * MockGeometryManager will always return this geometry
     * on calls to make().
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    /**
     * Calls to MockGeometryManager::make() will always
     * return the geometry assigned by the user, no matter
     * what the argument to Element.
     */
    public Geometry make(Element layerRoot) {
        return geometry;
    }
}
