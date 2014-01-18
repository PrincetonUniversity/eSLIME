package io.serialize;

import io.project.ProjectLoader;
import layers.LayerManager;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import structural.GeneralParameters;

/**
 * Created by David B Borenstein on 1/12/14.
 */
public class MockSerializationManager extends SerializationManager {

    public MockSerializationManager() {
        super(new BaseElement("dummy"), null, null);
    }
}
